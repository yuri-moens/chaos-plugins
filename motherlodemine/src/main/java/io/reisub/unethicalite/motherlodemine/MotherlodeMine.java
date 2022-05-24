package io.reisub.unethicalite.motherlodemine;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.entities.TileObjects;
import dev.unethicalite.api.game.GameThread;
import dev.unethicalite.api.game.Vars;
import dev.unethicalite.api.items.Inventory;
import dev.unethicalite.client.Static;
import io.reisub.unethicalite.motherlodemine.tasks.Deposit;
import io.reisub.unethicalite.motherlodemine.tasks.FixWheel;
import io.reisub.unethicalite.motherlodemine.tasks.GoDown;
import io.reisub.unethicalite.motherlodemine.tasks.GoToMiningArea;
import io.reisub.unethicalite.motherlodemine.tasks.GoUp;
import io.reisub.unethicalite.motherlodemine.tasks.HandleBank;
import io.reisub.unethicalite.motherlodemine.tasks.Mine;
import io.reisub.unethicalite.motherlodemine.tasks.UseShortcut;
import io.reisub.unethicalite.motherlodemine.tasks.WithdrawSack;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.enums.Activity;
import java.util.Set;
import javax.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.AnimationID;
import net.runelite.api.ItemID;
import net.runelite.api.ObjectID;
import net.runelite.api.Perspective;
import net.runelite.api.TileObject;
import net.runelite.api.Varbits;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

@PluginDescriptor(
    name = "Chaos Motherlode Mine",
    description = "Diggy, diggy hole",
    enabledByDefault = false)
@PluginDependency(Utils.class)
@Slf4j
@Extension
public class MotherlodeMine extends TickScript {

  private static final Set<Integer> MOTHERLODE_MAP_REGIONS =
      ImmutableSet.of(14679, 14680, 14681, 14935, 14936, 14937, 15191, 15192, 15193);
  private static final int SACK_LARGE_SIZE = 162;
  private static final int SACK_SIZE = 81;
  private static final int UPPER_FLOOR_HEIGHT = -490;

  @Inject
  private Config config;
  private int curSackSize;
  private int maxSackSize;
  @Getter
  private boolean sackFull;

  @Provides
  public Config getConfig(ConfigManager configManager) {
    return configManager.getConfig(Config.class);
  }

  @Override
  protected void onStart() {
    super.onStart();

    addTask(Mine.class);
    addTask(GoDown.class);
    addTask(FixWheel.class);
    addTask(Deposit.class);
    addTask(WithdrawSack.class);
    addTask(HandleBank.class);
    addTask(UseShortcut.class);
    addTask(GoToMiningArea.class);
    addTask(GoUp.class);
  }

  @Subscribe
  private void onAnimationChanged(AnimationChanged event) {
    Actor actor = event.getActor();
    if (!isRunning() || actor == null || actor != Players.getLocal()) {
      return;
    }

    switch (Players.getLocal().getAnimation()) {
      case AnimationID.MINING_MOTHERLODE_BRONZE:
      case AnimationID.MINING_MOTHERLODE_IRON:
      case AnimationID.MINING_MOTHERLODE_STEEL:
      case AnimationID.MINING_MOTHERLODE_BLACK:
      case AnimationID.MINING_MOTHERLODE_MITHRIL:
      case AnimationID.MINING_MOTHERLODE_ADAMANT:
      case AnimationID.MINING_MOTHERLODE_RUNE:
      case AnimationID.MINING_MOTHERLODE_DRAGON:
      case AnimationID.MINING_MOTHERLODE_DRAGON_OR:
      case AnimationID.MINING_MOTHERLODE_DRAGON_UPGRADED:
      case AnimationID.MINING_MOTHERLODE_CRYSTAL:
      case AnimationID.MINING_MOTHERLODE_GILDED:
      case AnimationID.MINING_MOTHERLODE_INFERNAL:
      case AnimationID.MINING_MOTHERLODE_3A:
        setActivity(Activity.MINING);
        break;
      default:
    }
  }

  @Subscribe
  private void onGameObjectDespawned(GameObjectDespawned event) {
    if (currentActivity == Activity.REPAIRING
        && event.getGameObject().getName().equals("Broken strut")) {
      setActivity(Activity.IDLE);
    }
  }

  @Subscribe
  private void onItemContainerChanged(ItemContainerChanged event) {
    if (currentActivity == Activity.DEPOSITING) {
      if (!Inventory.contains(ItemID.PAYDIRT)) {
        setActivity(Activity.IDLE);
      }
    } else if (currentActivity == Activity.WITHDRAWING) {
      if (Inventory.contains(
          ItemID.RUNITE_ORE,
          ItemID.ADAMANTITE_ORE,
          ItemID.MITHRIL_ORE,
          ItemID.GOLD_ORE,
          ItemID.COAL,
          ItemID.UNCUT_SAPPHIRE,
          ItemID.UNCUT_EMERALD,
          ItemID.UNCUT_RUBY,
          ItemID.UNCUT_DIAMOND,
          ItemID.UNCUT_DRAGONSTONE)) {
        setActivity(Activity.IDLE);
      }
    } else if (currentActivity == Activity.MINING) {
      if (Inventory.isFull()) {
        setActivity(Activity.IDLE);
      }
    }
  }

  @Subscribe
  private void onVarbitChanged(VarbitChanged event) {
    if (isRunning() && inMlm()) {
      refreshSackValues();
      if (curSackSize >= maxSackSize - 26) {
        sackFull = true;
      }
    }
  }

  public boolean isUpstairs() {
    return Perspective.getTileHeight(
        Static.getClient(), Players.getLocal().getLocalLocation(), 0) < UPPER_FLOOR_HEIGHT;
  }

  public void mineRockfall(final int x, final int y) {
    final TileObject rockfall = TileObjects.getFirstAt(x, y, 0,
        ObjectID.ROCKFALL, ObjectID.ROCKFALL_26680, ObjectID.ROCKFALL_28786);

    if (rockfall != null) {
      GameThread.invoke(() -> rockfall.interact("Mine"));
      Time.sleepTicksUntil(
          () -> TileObjects.getFirstAt(x, y, 0,
              ObjectID.ROCKFALL, ObjectID.ROCKFALL_26680, ObjectID.ROCKFALL_28786) == null, 50
      );
    }
  }

  public MiningArea getMiningArea() {
    if (config.upstairs()) {
      return MiningArea.UPSTAIRS;
    } else if (config.shortcut()) {
      return MiningArea.BEHIND_SHORTCUT;
    } else {
      return MiningArea.NORTH;
    }
  }

  private boolean inMlm() {
    return Utils.isInMapRegion(MOTHERLODE_MAP_REGIONS);
  }

  private void refreshSackValues() {
    curSackSize = Vars.getBit(Varbits.SACK_NUMBER);
    boolean sackUpgraded = Vars.getBit(Varbits.SACK_UPGRADED) == 1;
    maxSackSize = sackUpgraded ? SACK_LARGE_SIZE : SACK_SIZE;

    if (curSackSize == 0) {
      sackFull = false;
    }
  }
}
