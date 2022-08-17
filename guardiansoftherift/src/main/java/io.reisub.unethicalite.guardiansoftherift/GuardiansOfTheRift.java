package io.reisub.unethicalite.guardiansoftherift;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import io.reisub.unethicalite.guardiansoftherift.data.GuardianInfo;
import io.reisub.unethicalite.guardiansoftherift.data.PluginActivity;
import io.reisub.unethicalite.guardiansoftherift.tasks.CraftEssence;
import io.reisub.unethicalite.guardiansoftherift.tasks.CraftRunes;
import io.reisub.unethicalite.guardiansoftherift.tasks.EnterAltar;
import io.reisub.unethicalite.guardiansoftherift.tasks.EnterPortal;
import io.reisub.unethicalite.guardiansoftherift.tasks.GoToLargeRemains;
import io.reisub.unethicalite.guardiansoftherift.tasks.LeaveAltar;
import io.reisub.unethicalite.guardiansoftherift.tasks.LeaveHugeRemains;
import io.reisub.unethicalite.guardiansoftherift.tasks.LeaveLargeRemains;
import io.reisub.unethicalite.guardiansoftherift.tasks.MineGuardianParts;
import io.reisub.unethicalite.guardiansoftherift.tasks.MineHugeRemains;
import io.reisub.unethicalite.guardiansoftherift.tasks.MineLargeRemains;
import io.reisub.unethicalite.guardiansoftherift.tasks.PlaceCell;
import io.reisub.unethicalite.guardiansoftherift.tasks.PowerGuardian;
import io.reisub.unethicalite.guardiansoftherift.tasks.RepairPouches;
import io.reisub.unethicalite.guardiansoftherift.tasks.TakeStartItems;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.Animation;
import net.runelite.api.AnimationID;
import net.runelite.api.Client;
import net.runelite.api.DynamicObject;
import net.runelite.api.GameObject;
import net.runelite.api.ItemID;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.widgets.Widgets;
import net.unethicalite.client.Static;
import org.pf4j.Extension;
import org.slf4j.Logger;

@PluginDescriptor(
    name = "Chaos Guardians of the Rift",
    description = "Plays the Guardians of the Rift minigame",
    enabledByDefault = false
)
@PluginDependency(Utils.class)
@Slf4j
@Extension
public class GuardiansOfTheRift extends TickScript {

  private static final int WIDGET_GROUP_ID = 746;
  private static final int GUARDIAN_ACTIVE_ANIM = 9363;
  private static final Set<Integer> GUARDIAN_IDS =
      ImmutableSet.of(43705, 43701, 43710, 43702, 43703, 43711, 43704, 43708, 43712, 43707, 43706,
          43709);
  private static final int PORTAL_WIDGET_ID = 48889883;
  private static final Set<String> GAME_END_MESSAGES = ImmutableSet.of(
      "The Great Guardian successfully closed the rift!",
      "The Portal Guardians close their rifts.",
      "The Great Guardian was defeated!",
      "The Portal Guardians will keep their rifts open for another 30 seconds."
  );
  @Getter
  private final Set<GameObject> activeGuardians = new HashSet<>();
  @Getter
  private final Set<GameObject> guardians = new HashSet<>();
  @Inject
  private Config config;
  @Inject
  private Overlay overlay;
  @Inject
  private Client client;
  @Inject
  private OverlayManager overlayManager;
  @Getter
  private boolean portalActive;
  private int startTick = -1;
  private int lastPouchFullMessageTick;
  private int lastPouchEmptyMessageTick;
  private int fullPouches;
  private int emptyPouches;

  @Provides
  public Config getConfig(ConfigManager configManager) {
    return configManager.getConfig(Config.class);
  }

  @Override
  public Logger getLogger() {
    return log;
  }

  @Override
  protected void onStart() {
    super.onStart();

    reset();

    overlayManager.add(overlay);

    for (GuardianInfo gi : GuardianInfo.ALL) {
      log.info(String.valueOf(gi.haveRequirements()));
    }

    addTask(RepairPouches.class);
    addTask(TakeStartItems.class);
    addTask(GoToLargeRemains.class);
    addTask(MineLargeRemains.class);
    addTask(LeaveLargeRemains.class);

    addTask(LeaveAltar.class);
    addTask(CraftRunes.class);

    addTask(LeaveHugeRemains.class);
    addTask(MineHugeRemains.class);

    addTask(PowerGuardian.class);
    addTask(PlaceCell.class);
    addTask(EnterPortal.class);
    addTask(CraftEssence.class);
    addTask(EnterAltar.class);

    addTask(MineGuardianParts.class);

  }

  @Override
  protected void onStop() {
    super.onStop();

    overlayManager.remove(overlay);
  }

  @Subscribe
  private void onAnimationChanged(AnimationChanged event) {
    Actor actor = event.getActor();
    if (!isRunning() || actor == null || actor != Players.getLocal()) {
      return;
    }

    switch (Players.getLocal().getAnimation()) {
      case AnimationID.MINING_BRONZE_PICKAXE:
      case AnimationID.MINING_IRON_PICKAXE:
      case AnimationID.MINING_STEEL_PICKAXE:
      case AnimationID.MINING_BLACK_PICKAXE:
      case AnimationID.MINING_MITHRIL_PICKAXE:
      case AnimationID.MINING_ADAMANT_PICKAXE:
      case AnimationID.MINING_RUNE_PICKAXE:
      case AnimationID.MINING_DRAGON_PICKAXE:
      case AnimationID.MINING_DRAGON_PICKAXE_OR:
      case AnimationID.MINING_DRAGON_PICKAXE_UPGRADED:
      case AnimationID.MINING_CRYSTAL_PICKAXE:
      case AnimationID.MINING_GILDED_PICKAXE:
      case AnimationID.MINING_INFERNAL_PICKAXE:
      case AnimationID.MINING_3A_PICKAXE:
        setActivity(PluginActivity.MINING);
        break;
      default:
    }
  }

  @Subscribe
  public void onGameTick(GameTick tick) {
    activeGuardians.removeIf(ag -> {
      Animation anim = ((DynamicObject) ag.getRenderable()).getAnimation();
      return anim == null || anim.getId() != GUARDIAN_ACTIVE_ANIM
          || !Inventory.contains(GuardianInfo.getForObjectId(ag.getId()).getTalismanId());
    });

    for (GameObject guardian : guardians) {
      Animation animation = ((DynamicObject) guardian.getRenderable()).getAnimation();
      if ((animation != null && animation.getId() == GUARDIAN_ACTIVE_ANIM)
          || Inventory.contains(GuardianInfo.getForObjectId(guardian.getId()).getTalismanId())) {
        activeGuardians.add(guardian);
      }
    }

    Widget portalWidget = Widgets.fromId(PORTAL_WIDGET_ID);

    portalActive = portalWidget != null && !portalWidget.isHidden();
  }

  @Subscribe
  public void onGameObjectSpawned(GameObjectSpawned event) {
    GameObject gameObject = event.getGameObject();
    if (GUARDIAN_IDS.contains(event.getGameObject().getId())) {
      guardians.removeIf(g -> g.getId() == gameObject.getId());
      activeGuardians.removeIf(g -> g.getId() == gameObject.getId());
      guardians.add(gameObject);
    }
  }

  @Subscribe
  public void onChatMessage(ChatMessage chatMessage) {
    String msg = chatMessage.getMessage();
    if (msg.contains("The rift becomes active!")) {
      startTick = Static.getClient().getTickCount();
    } else if (GAME_END_MESSAGES.contains(msg)) {
      reset();
    } else if (msg.equals("You cannot add any more essence to the pouch.")) {
      if (Static.getClient().getTickCount() != lastPouchFullMessageTick) {
        fullPouches = 0;
      }

      lastPouchFullMessageTick = Static.getClient().getTickCount();
      fullPouches++;
      emptyPouches = 0;
    } else if (msg.equals("There is no essence in this pouch.")) {
      if (Static.getClient().getTickCount() != lastPouchEmptyMessageTick) {
        emptyPouches = 0;
      }

      lastPouchEmptyMessageTick = Static.getClient().getTickCount();
      emptyPouches++;
      fullPouches = 0;
    }
  }

  private void reset() {
    startTick = -1;

    if (Inventory.contains("Medium cell", "Strong cell", "Overcharged cell")) {
      Inventory.getFirst("Medium cell", "Strong cell", "Overcharged cell").drop();
    }

    if (Inventory.contains("Guardian essence")) {
      Inventory.getFirst("Guardian essence").drop();
    }
  }

  public GuardianInfo getBestGuardian() {
    Set<GuardianInfo> activeGuardiansInfo = new HashSet<>();
    for (GameObject ag : activeGuardians) {
      GuardianInfo gii = GuardianInfo.getForObjectId(ag.getId());
      if (gii != null && gii.haveRequirements()) {
        activeGuardiansInfo.add(gii);
      }
    }
    if (config.focusPoints() && activeGuardiansInfo.size() > 1) {
      activeGuardiansInfo.removeIf(gf -> config.runeTypeFocus() == gf.getRuneType());
    }

    return activeGuardiansInfo
        .stream()
        .max(Comparator.comparing(GuardianInfo::getCellType))
        .isPresent()
        ? activeGuardiansInfo
        .stream()
        .max(Comparator.comparing(GuardianInfo::getCellType))
        .get() : null;
  }

  public int getElapsedTicks() {
    if (startTick == -1) {
      return -1;
    }

    return Static.getClient().getTickCount() - startTick;
  }

  private int getWidgetInteger(int widgetId) {
    final Widget widget = Widgets.get(WIDGET_GROUP_ID, widgetId);

    if (widget == null) {
      return -1;
    }

    final String[] split = widget.getText().split(":");

    if (split.length != 2) {
      return -1;
    }

    try {
      final String string = split[1]
          .replace("%", "")
          .trim();

      return Integer.parseInt(string);
    } catch (NumberFormatException e) {
      log.debug("Failed to parse number from string: {}", split[1]);
      return -1;
    }
  }

  public int getEntranceTimer() {
    return getWidgetInteger(5);
  }

  public int getGuardianPower() {
    return getWidgetInteger(18);
  }

  public int getElementalEnergy() {
    return getWidgetInteger(21);
  }

  public int getCatalyticEnergy() {
    return getWidgetInteger(24);
  }

  public int getPortalTimer() {
    return getWidgetInteger(26);
  }

  public boolean arePouchesFull() {
    final Set<Integer> normalPouches = new HashSet<>(Constants.ESSENCE_POUCH_IDS);
    normalPouches.removeAll(Constants.DEGRADED_ESSENCE_POUCH_IDS);

    final int pouchesCount = Inventory.getCount(Predicates.ids(normalPouches));

    return fullPouches >= pouchesCount
        || (fullPouches > 0 && Inventory.contains(ItemID.GUARDIAN_ESSENCE));
  }

  public boolean arePouchesEmpty() {
    final Set<Integer> normalPouches = new HashSet<>(Constants.ESSENCE_POUCH_IDS);
    normalPouches.removeAll(Constants.DEGRADED_ESSENCE_POUCH_IDS);

    final int pouchesCount = Inventory.getCount(Predicates.ids(normalPouches));

    return emptyPouches >= pouchesCount
        || (emptyPouches > 0 && Inventory.getFreeSlots() > 0);
  }
}
