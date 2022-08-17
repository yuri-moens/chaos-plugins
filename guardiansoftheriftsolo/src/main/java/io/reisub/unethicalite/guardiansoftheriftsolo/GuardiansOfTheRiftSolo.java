package io.reisub.unethicalite.guardiansoftheriftsolo;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import io.reisub.unethicalite.guardiansoftheriftsolo.data.PluginActivity;
import io.reisub.unethicalite.guardiansoftheriftsolo.data.RuneType;
import io.reisub.unethicalite.guardiansoftheriftsolo.tasks.BuildBarrier;
import io.reisub.unethicalite.guardiansoftheriftsolo.tasks.BuildGuardian;
import io.reisub.unethicalite.guardiansoftheriftsolo.tasks.CraftCell;
import io.reisub.unethicalite.guardiansoftheriftsolo.tasks.CraftEssence;
import io.reisub.unethicalite.guardiansoftheriftsolo.tasks.CraftRunes;
import io.reisub.unethicalite.guardiansoftheriftsolo.tasks.DepositRunes;
import io.reisub.unethicalite.guardiansoftheriftsolo.tasks.EnterAltar;
import io.reisub.unethicalite.guardiansoftheriftsolo.tasks.EnterPortal;
import io.reisub.unethicalite.guardiansoftheriftsolo.tasks.GoToLargeRemains;
import io.reisub.unethicalite.guardiansoftheriftsolo.tasks.LeaveAltar;
import io.reisub.unethicalite.guardiansoftheriftsolo.tasks.LeaveHugeRemains;
import io.reisub.unethicalite.guardiansoftheriftsolo.tasks.LeaveLargeRemains;
import io.reisub.unethicalite.guardiansoftheriftsolo.tasks.MineHugeRemains;
import io.reisub.unethicalite.guardiansoftheriftsolo.tasks.MineLargeRemains;
import io.reisub.unethicalite.guardiansoftheriftsolo.tasks.PowerGuardian;
import io.reisub.unethicalite.guardiansoftheriftsolo.tasks.RechargeBarrier;
import io.reisub.unethicalite.guardiansoftheriftsolo.tasks.RepairPouches;
import io.reisub.unethicalite.guardiansoftheriftsolo.tasks.TakeCell;
import io.reisub.unethicalite.guardiansoftheriftsolo.tasks.TakeItems;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.AnimationID;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.widgets.Widgets;
import net.unethicalite.client.Static;
import org.pf4j.Extension;
import org.slf4j.Logger;

@PluginDescriptor(
    name = "Chaos Guardians of the Rift Solo",
    description = "Plays the Guardians of the Rift minigame solo",
    enabledByDefault = false
)
@PluginDependency(Utils.class)
@Slf4j
@Extension
public class GuardiansOfTheRiftSolo extends TickScript {

  private static final WorldPoint NEAR_START = new WorldPoint(3615, 9489, 0);
  private static final int WIDGET_GROUP_ID = 746;
  private static final Set<String> GAME_END_MESSAGES = ImmutableSet.of(
      "The Great Guardian successfully closed the rift!",
      "The Portal Guardians close their rifts.",
      "The Great Guardian was defeated!",
      "The Portal Guardians will keep their rifts open for another 30 seconds."
  );

  @Inject
  private Config config;
  private int startTick;
  @Getter
  @Setter
  private RuneType lastGuardianBuild;
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

    addTask(TakeItems.class);
    addTask(GoToLargeRemains.class);
    addTask(MineLargeRemains.class);
    addTask(LeaveLargeRemains.class);
    addTask(DepositRunes.class);
    addTask(TakeCell.class);
    addTask(BuildGuardian.class);
    addTask(BuildBarrier.class);
    addTask(RechargeBarrier.class);
    addTask(PowerGuardian.class);
    addTask(RepairPouches.class);
    addTask(CraftCell.class);
    addTask(CraftRunes.class);
    addTask(LeaveAltar.class);
    addTask(MineHugeRemains.class);
    addTask(LeaveHugeRemains.class);
    addTask(EnterPortal.class);
    addTask(EnterAltar.class);
    addTask(CraftEssence.class);
  }

  @Subscribe
  private void onGameTick(GameTick event) {
    // log.info("ticks elapsed: " + getElapsedTicks());
    // log.info("seconds elapsed: " + getElapsedTicks() * 0.6);
  }

  @Subscribe
  private void onChatMessage(ChatMessage event) {
    if (!isRunning()) {
      return;
    }

    final String message = event.getMessage();

    if (message.contains("The rift becomes active!")) {
      startTick = Static.getClient().getTickCount();
    } else if (GAME_END_MESSAGES.contains(message)) {
      reset();
    } else if (message.equals("You cannot add any more essence to the pouch.")) {
      if (Static.getClient().getTickCount() != lastPouchFullMessageTick) {
        fullPouches = 0;
      }

      lastPouchFullMessageTick = Static.getClient().getTickCount();
      fullPouches++;
    } else if (message.equals("There is no essence in this pouch.")) {
      if (Static.getClient().getTickCount() != lastPouchEmptyMessageTick) {
        emptyPouches = 0;
      }

      emptyPouches++;
    }
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

  private void reset() {
    startTick = -1;
    lastGuardianBuild = null;
  }

  public int getElapsedTicks() {
    if (startTick == -1) {
      return -1;
    }

    return Static.getClient().getTickCount() - startTick;
  }

  public boolean isGameActive() {
    return startTick != -1;
  }

  public boolean isNearStart() {
    return isNearStart(5);
  }

  public boolean isNearStart(int distance) {
    return Players.getLocal().distanceTo(NEAR_START) < 5;
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

  public int getGuardianCount() {
    final Widget widget = Widgets.get(WIDGET_GROUP_ID, 30);

    if (widget == null) {
      return -1;
    }

    final String[] split = widget.getText().split("/");

    if (split.length != 2) {
      return -1;
    }

    try {
      final String string = split[0].trim();

      return Integer.parseInt(string);
    } catch (NumberFormatException e) {
      log.debug("Failed to parse number from string: {}", split[0]);
      return -1;
    }
  }

  public boolean arePouchesFull() {
    final Set<Integer> normalPouches = new HashSet<>(Constants.ESSENCE_POUCH_IDS);
    normalPouches.removeAll(Constants.DEGRADED_ESSENCE_POUCH_IDS);

    final int pouchesCount = Inventory.getCount(Predicates.ids(normalPouches));

    return fullPouches >= pouchesCount;
  }

  public boolean arePouchesEmpty() {
    final Set<Integer> normalPouches = new HashSet<>(Constants.ESSENCE_POUCH_IDS);
    normalPouches.removeAll(Constants.DEGRADED_ESSENCE_POUCH_IDS);

    final int pouchesCount = Inventory.getCount(Predicates.ids(normalPouches));

    return emptyPouches >= pouchesCount;
  }
}
