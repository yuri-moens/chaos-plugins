package io.reisub.unethicalite.guardiansoftherift;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import io.reisub.unethicalite.guardiansoftherift.tasks.CraftRunes;
import io.reisub.unethicalite.guardiansoftherift.tasks.GoThroughPortal;
import io.reisub.unethicalite.guardiansoftherift.tasks.GoToAltar;
import io.reisub.unethicalite.guardiansoftherift.tasks.MakeEssence;
import io.reisub.unethicalite.guardiansoftherift.tasks.MineGuardianParts;
import io.reisub.unethicalite.guardiansoftherift.tasks.MineHugeRemains;
import io.reisub.unethicalite.guardiansoftherift.tasks.MineLargeRemains;
import io.reisub.unethicalite.guardiansoftherift.tasks.MoveToFirstCell;
import io.reisub.unethicalite.guardiansoftherift.tasks.MoveToLargeRemains;
import io.reisub.unethicalite.guardiansoftherift.tasks.MoveToMainArea;
import io.reisub.unethicalite.guardiansoftherift.tasks.PlaceCell;
import io.reisub.unethicalite.guardiansoftherift.tasks.PlaceFirstCell;
import io.reisub.unethicalite.guardiansoftherift.tasks.PowerGuardian;
import io.reisub.unethicalite.guardiansoftherift.tasks.ReturnToMainArea;
import io.reisub.unethicalite.guardiansoftherift.tasks.ReturnToMainAreaFromHugeRemains;
import io.reisub.unethicalite.guardiansoftherift.tasks.StartItems;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Animation;
import net.runelite.api.Client;
import net.runelite.api.DynamicObject;
import net.runelite.api.GameObject;
import net.runelite.api.ItemID;
import net.runelite.api.Skill;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.Skills;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.widgets.Widgets;
import org.pf4j.Extension;

@PluginDescriptor(
    name = "Chaos GuardiansOfTheRift",
    description = "GOTR",
    enabledByDefault = false
)
@PluginDependency(Utils.class)
@Slf4j
@Extension
public class GuardiansOfTheRift extends TickScript {
  public static final int TIMER_WIDGET_ID = 48889861;
  public static final Set<Integer> POUCH_IDS =
      ImmutableSet.of(ItemID.SMALL_POUCH, ItemID.MEDIUM_POUCH, ItemID.MEDIUM_POUCH_5511,
          ItemID.LARGE_POUCH, ItemID.LARGE_POUCH_5513, ItemID.GIANT_POUCH, ItemID.GIANT_POUCH_5515,
          ItemID.COLOSSAL_POUCH);
  private static final int GUARDIAN_ACTIVE_ANIM = 9363;
  private static final Set<Integer> GUARDIAN_IDS =
      ImmutableSet.of(43705, 43701, 43710, 43702, 43703, 43711, 43704, 43708, 43712, 43707, 43706,
          43709, 43702);
  private static final int MINIGAME_MAIN_REGION = 14484;
  private static final int PORTAL_WIDGET_ID = 48889883;
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
  // 1: Countdown to new game
  // 2: Positioning for start
  // 3: Game started
  // 4: Mining inital fragments
  // 5: done mining
  // 10: main game
  @Getter
  @Setter
  private int gamePhase;
  @Getter
  private boolean portalActive;

  @Provides
  public Config getConfig(ConfigManager configManager) {
    return configManager.getConfig(Config.class);
  }

  @Override
  protected void onStart() {
    super.onStart();

    overlayManager.add(overlay);


    addTask(StartItems.class);
    addTask(MoveToFirstCell.class);
    addTask(PlaceFirstCell.class);
    addTask(MoveToLargeRemains.class);
    addTask(MineLargeRemains.class);
    addTask(MoveToMainArea.class);

    addTask(ReturnToMainArea.class);
    addTask(CraftRunes.class);

    addTask(ReturnToMainAreaFromHugeRemains.class);
    addTask(MineHugeRemains.class);
    addTask(GoThroughPortal.class);

    addTask(PowerGuardian.class);
    addTask(PlaceCell.class);
    addTask(MakeEssence.class);
    addTask(GoToAltar.class);

    addTask(MineGuardianParts.class);

  }

  @Override
  protected void onStop() {
    super.onStop();

    setGamePhase(0);

    overlayManager.remove(overlay);
  }

  public boolean checkInMainRegion() {
    int[] currentMapRegions = client.getMapRegions();
    return Arrays.stream(currentMapRegions).anyMatch(x -> x == MINIGAME_MAIN_REGION);
  }

  @Subscribe
  public void onGameTick(GameTick tick) {
    activeGuardians.removeIf(ag -> {
      Animation anim = ((DynamicObject) ag.getRenderable()).getAnimation();
      return anim == null || anim.getId() != GUARDIAN_ACTIVE_ANIM;
    });

    for (GameObject guardian : guardians) {
      Animation animation = ((DynamicObject) guardian.getRenderable()).getAnimation();
      if (animation != null && animation.getId() == GUARDIAN_ACTIVE_ANIM) {
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
      setGamePhase(3);
    } else if (msg.contains("The rift will become active in 30 seconds.")) {
      if (getGamePhase() == 0) {
        setGamePhase(1);
      }
    } else if (msg.contains("The rift will become active in 10 seconds.")) {
      if (getGamePhase() == 0) {
        setGamePhase(1);
      }
    } else if (msg.contains("The rift will become active in 5 seconds.")) {
      if (getGamePhase() == 0) {
        setGamePhase(1);
      }
    } else if (msg.contains("The Great Guardian successfully closed the rift!")) {
      reset();
    } else if (msg.contains("The Portal Guardians close their rifts.")) {
      reset();
    } else if (msg.contains("The Great Guardian was defeated!")) {
      reset();
    } else if (msg.contains(
        "The Portal Guardians will keep their rifts open for another 30 seconds.")) {
      if (getGamePhase() == 0) {
        setGamePhase(100);
      }
      reset();
    }

  }

  private void reset() {
    setGamePhase(0);
    if (Players.getLocal().getWorldLocation().getWorldX() < 3597) {
      TileObjects.getNearest("Portal").interact("Use");
      Time.sleepTicksUntil(this::checkInMainRegion, 20);
      Time.sleepTick();
    }
    if (Inventory.contains("Medium cell", "Strong cell", "Overcharged cell")) {
      Inventory.getFirst("Medium cell", "Strong cell", "Overcharged cell").drop();
    }
  }

  public GuardianInfo getBestGuardian() {
    Set<GuardianInfo> activeGuardiansInfo = new HashSet<>();
    for (GameObject ag : activeGuardians) {
      GuardianInfo gii = GuardianInfo.getForObjectId(ag.getId());
      if (gii != null && gii.levelRequired <= Skills.getLevel(Skill.RUNECRAFT)) {
        activeGuardiansInfo.add(gii);
      }
    }
    return activeGuardiansInfo.stream().max(Comparator.comparingInt(GuardianInfo::getLevelRequired))
        .isPresent()
        ?
        activeGuardiansInfo.stream().max(Comparator.comparingInt(GuardianInfo::getLevelRequired))
            .get() : null;
  }
}
