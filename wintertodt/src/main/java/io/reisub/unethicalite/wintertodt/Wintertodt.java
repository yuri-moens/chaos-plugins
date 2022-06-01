package io.reisub.unethicalite.wintertodt;

import com.google.inject.Provides;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.game.Skills;
import net.unethicalite.api.game.Vars;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.widgets.Widgets;
import net.unethicalite.client.Static;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.enums.Activity;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.AnimationID;
import net.runelite.api.ChatMessageType;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemContainer;
import net.runelite.api.ItemID;
import net.runelite.api.MessageNode;
import net.runelite.api.Projectile;
import net.runelite.api.Skill;
import net.runelite.api.Varbits;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.ProjectileMoved;
import net.runelite.api.events.StatChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

@PluginDescriptor(
    name = "Chaos Wintertodt",
    description = "The cold of the Wintertodt seeps into your bones",
    enabledByDefault = false
)
@PluginDependency(Utils.class)
@Slf4j
@Extension
public class Wintertodt extends TickScript {

  public static final int WINTERTODT_REGION = 6462;
  public static final int WINTERTODT_HEALTH_PACKED_ID = 25952277;
  public static final int WINTERTODT_GAME_TIMER_ID = 25952259;

  @Getter
  private int respawnTimer;
  @Getter
  private int bossHealth;
  @Getter
  @Setter
  private boolean tooCold;
  @Getter
  @Setter
  private Instant lastHop;
  @Getter
  private final List<WintertodtProjectile> projectiles = new ArrayList<>();
  private Hop hopTask;
  private Scouter scouter;
  private int fmLevel, wcLevel, fletchLevel;
  private int previousTimerValue;

  @Inject
  private Config config;

  @Provides
  public Config getConfig(ConfigManager configManager) {
    return configManager.getConfig(Config.class);
  }


  @Override
  protected void onStart() {
    super.onStart();
    fmLevel = Skills.getLevel(Skill.FIREMAKING);
    wcLevel = Skills.getLevel(Skill.WOODCUTTING);
    fletchLevel = Skills.getLevel(Skill.FLETCHING);

    if (config.hop()) {
      scouter = injector.getInstance(Scouter.class);
      Static.getEventBus().register(scouter);
    }

    addTask(DodgeProjectile.class);
    addTask(OpenInventory.class);
    addTask(EatWhileWaiting.class);
    addTask(OpenCrates.class);
    addTask(GoToBank.class);
    addTask(HandleBank.class);
    addTask(GoToWintertodt.class);

    if (config.hop()) {
      hopTask = injector.getInstance(Hop.class);
      tasks.add(hopTask);
    }

    addTask(MoveToBrazier.class);
    addTask(Fix.class);
    addTask(Light.class);
    addTask(Fletch.class);
    addTask(ChangeSide.class);
    addTask(Burn.class);
    addTask(Chop.class);
  }

  @Override
  protected void onStop() {
    super.onStop();

    if (scouter != null) {
      Static.getEventBus().unregister(scouter);
      scouter = null;
    }
  }

  @Subscribe
  private void onStatChanged(final StatChanged event) {
    final Skill skill = event.getSkill();
    final int level = event.getLevel();

    if (skill == Skill.FIREMAKING && level > fmLevel) {
      setActivity(Activity.IDLE);
      fmLevel = event.getLevel();
    } else if (skill == Skill.WOODCUTTING && level > wcLevel) {
      setActivity(Activity.IDLE);
      wcLevel = event.getLevel();
    } else if (skill == Skill.FLETCHING && level > fletchLevel) {
      setActivity(Activity.IDLE);
      fletchLevel = event.getLevel();
    } else if (skill == Skill.FIREMAKING && currentActivity == Activity.LIGHTING_BRAZIER) {
      setActivity(Activity.IDLE);
    } else if (skill == Skill.CONSTRUCTION && currentActivity == Activity.FIXING_BRAZIER) {
      setActivity(Activity.IDLE);
    }
  }

  @Subscribe
  private void onGameTick(final GameTick event) {
    if (!isRunning() || !isInWintertodtRegion()) {
      return;
    }

    parseBossHealth();
  }

  @Subscribe
  private void onChatMessage(final ChatMessage chatMessage) {
    if (!isRunning() || !isInWintertodtRegion()) {
      return;
    }

    final ChatMessageType chatMessageType = chatMessage.getType();

    if (chatMessageType != ChatMessageType.GAMEMESSAGE && chatMessageType != ChatMessageType.SPAM) {
      return;
    }

    final MessageNode messageNode = chatMessage.getMessageNode();
    final InterruptType interruptType;

    if (messageNode.getValue().startsWith("You carefully fletch the root")) {
      setActivity(Activity.FLETCHING);
      return;
    }

    if (messageNode.getValue().startsWith("The cold of")) {
      interruptType = InterruptType.COLD;
    } else if (messageNode.getValue().startsWith("The freezing cold attack")) {
      interruptType = InterruptType.SNOWFALL;
    } else if (messageNode.getValue().startsWith("The brazier is broken and shrapnel")) {
      interruptType = InterruptType.BRAZIER;
    } else if (messageNode.getValue().startsWith("You have run out of bruma roots")) {
      interruptType = InterruptType.OUT_OF_ROOTS;
    } else if (messageNode.getValue().startsWith("Your inventory is too full")) {
      interruptType = InterruptType.INVENTORY_FULL;
    } else if (messageNode.getValue().startsWith("You fix the brazier")) {
      interruptType = InterruptType.FIXED_BRAZIER;
    } else if (messageNode.getValue().startsWith("You light the brazier.")) {
      interruptType = InterruptType.LIT_BRAZIER;
    } else if (messageNode.getValue().startsWith("The brazier has gone out.")) {
      interruptType = InterruptType.BRAZIER_WENT_OUT;
    } else if (messageNode.getValue().startsWith("You eat")) {
      interruptType = InterruptType.EAT;
    } else if (messageNode.getValue().startsWith("Your hands are too cold")) {
      interruptType = InterruptType.TOO_COLD;
      tooCold = true;
    } else {
      return;
    }

    boolean wasInterrupted = false;
    switch (interruptType) {
      case EAT:
      case LIT_BRAZIER:
      case INVENTORY_FULL:
      case OUT_OF_ROOTS:
      case BRAZIER_WENT_OUT:
      case FIXED_BRAZIER:
        wasInterrupted = true;
        break;
      case COLD:
      case BRAZIER:
      case SNOWFALL:
        if (currentActivity != Activity.WOODCUTTING && currentActivity != Activity.IDLE) {
          wasInterrupted = true;
        }
        break;
      default:
        break;
    }

    if (wasInterrupted) {
      setActivity(Activity.IDLE);
    }
  }

  @Subscribe
  private void onAnimationChanged(final AnimationChanged event) {
    if (!isRunning() || !isInWintertodtRegion() || event.getActor() != Players.getLocal()) {
      return;
    }

    final int animId = Players.getLocal().getAnimation();
    switch (animId) {
      case AnimationID.WOODCUTTING_BRONZE:
      case AnimationID.WOODCUTTING_IRON:
      case AnimationID.WOODCUTTING_STEEL:
      case AnimationID.WOODCUTTING_BLACK:
      case AnimationID.WOODCUTTING_MITHRIL:
      case AnimationID.WOODCUTTING_ADAMANT:
      case AnimationID.WOODCUTTING_RUNE:
      case AnimationID.WOODCUTTING_GILDED:
      case AnimationID.WOODCUTTING_DRAGON:
      case AnimationID.WOODCUTTING_DRAGON_OR:
      case AnimationID.WOODCUTTING_INFERNAL:
      case AnimationID.WOODCUTTING_3A_AXE:
      case AnimationID.WOODCUTTING_CRYSTAL:
      case AnimationID.WOODCUTTING_TRAILBLAZER:
        setActivity(Activity.WOODCUTTING);
        break;
      case AnimationID.FLETCHING_BOW_CUTTING:
        setActivity(Activity.FLETCHING);
        break;
      case AnimationID.LOOKING_INTO:
        setActivity(Activity.FEEDING_BRAZIER);
        break;
      case AnimationID.FIREMAKING:
        setActivity(Activity.LIGHTING_BRAZIER);
        break;
      case AnimationID.CONSTRUCTION:
      case AnimationID.CONSTRUCTION_IMCANDO:
        setActivity(Activity.FIXING_BRAZIER);
        break;
    }
  }

  @Subscribe
  private void onItemContainerChanged(final ItemContainerChanged event) {
    if (!isRunning()) {
      return;
    }

    final ItemContainer container = event.getItemContainer();
    if (!isInWintertodtRegion() || container != Static.getClient()
        .getItemContainer(InventoryID.INVENTORY)) {
      return;
    }

    final int logs = Inventory.getCount(ItemID.BRUMA_ROOT);
    final int kindling = Inventory.getCount(ItemID.BRUMA_KINDLING);

    if (logs == 0 && currentActivity == Activity.FLETCHING) {
      setActivity(Activity.IDLE);
    } else if (logs == 0 && kindling == 0 && currentActivity == Activity.FEEDING_BRAZIER) {
      setActivity(Activity.IDLE);
    } else if (shouldStartFeeding() && (currentActivity == Activity.WOODCUTTING
        || currentActivity == Activity.FLETCHING)) {
      setActivity(Activity.IDLE);
    } else if (shouldStartFletching() && currentActivity == Activity.WOODCUTTING) {
      setActivity(Activity.IDLE);
    }
  }

  @Subscribe
  private void onVarbitChanged(final VarbitChanged varbitChanged) {
    if (!isRunning()) {
      return;
    }

    final int timerValue = Vars.getBit(Varbits.WINTERTODT_TIMER);

    if (timerValue != previousTimerValue) {
      respawnTimer = timerValue * 30 / 50;
      previousTimerValue = timerValue;
    }
  }

  @Subscribe
  private void onProjectileMoved(final ProjectileMoved event) {
    if (!isRunning()) {
      return;
    }

    final Projectile projectile = event.getProjectile();
    if (projectile.getInteracting() != null) {
      return;
    }

    if (projectile.getId() == 501) {
      int x = Static.getClient().getBaseX() + event.getPosition().getSceneX();
      int y = Static.getClient().getBaseY() + event.getPosition().getSceneY();
      int cycles = projectile.getEndCycle() - projectile.getStartCycle();
      // we don't care about any of the projectiles that don't go to our play area
      if (y >= 4001) {
        return;
      }
      if (cycles == 200) {
        projectiles.add(new WintertodtProjectile(x, y, true, Instant.now()));
      } else if ((x == 1638 && y == 3997) || (x == 1619 && y == 3997)) {
        if (cycles == 120) {
          projectiles.add(new WintertodtProjectile(x, y, false, Instant.now()));
        }
      }
    }
  }

  public boolean isInWintertodtRegion() {
    return Utils.isInRegion(WINTERTODT_REGION);
  }

  public boolean bossIsUp() {
    return isInWintertodtRegion()
        && getBossHealth() > 0
        && getRespawnTimer() <= 0;
  }

  public boolean shouldStartFletching() {
    final long rootCount = Inventory.getCount(ItemID.BRUMA_ROOT);
    final long kindlingCount = Inventory.getCount(ItemID.BRUMA_KINDLING);

    return rootCount * 2 + kindlingCount >= getBossHealth();
  }

  public boolean shouldStartFeeding() {
    final long burnablesCount = Inventory.getCount(Predicates.nameContains("Bruma"));

    return burnablesCount >= getBossHealth();
  }

  private void parseBossHealth() {
    final Widget healthWidget = Widgets.fromId(WINTERTODT_HEALTH_PACKED_ID);

    if (healthWidget != null) {
      final Pattern regex = Pattern.compile("\\d+");
      final Matcher bossHealthMatcher = regex.matcher(healthWidget.getText());

      if (bossHealthMatcher.find()) {
        try {
          bossHealth = Integer.parseInt(bossHealthMatcher.group(0));
        } catch (NumberFormatException e) {
          bossHealth = -1;
        }

        if (bossHealth > 0) {
          respawnTimer = -1;
        }
      } else {
        bossHealth = -1;
      }
    }
  }

  private void parseRespawnTime() {
    final Widget timerWidget = Widgets.fromId(WINTERTODT_GAME_TIMER_ID);

    if (timerWidget != null) {
      final Pattern regex = Pattern.compile("\\d:\\d+");
      final Matcher timerMatcher = regex.matcher(timerWidget.getText());

      if (timerMatcher.find()) {
        final String[] time = timerMatcher.group(0).split(":");
        final String minutes = time[0];
        final String seconds = time[1];

        try {
          respawnTimer = Integer.parseInt(minutes) * 60 + Integer.parseInt(seconds);
        } catch (NumberFormatException e) {
          respawnTimer = -1;
        }
      } else {
        respawnTimer = -1;
      }
    }
  }
}
