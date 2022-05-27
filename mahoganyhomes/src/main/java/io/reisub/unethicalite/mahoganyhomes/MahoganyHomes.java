package io.reisub.unethicalite.mahoganyhomes;

import com.google.inject.Provides;
import io.reisub.unethicalite.mahoganyhomes.tasks.Fix;
import io.reisub.unethicalite.mahoganyhomes.tasks.GetTask;
import io.reisub.unethicalite.mahoganyhomes.tasks.GoToHome;
import io.reisub.unethicalite.mahoganyhomes.tasks.HandleBank;
import io.reisub.unethicalite.mahoganyhomes.tasks.TalkToNpc;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.api.ChaosMovement;
import io.reisub.unethicalite.utils.api.Interact;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.TileObject;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.movement.Reachable;
import net.unethicalite.client.Static;
import org.pf4j.Extension;

@PluginDescriptor(
    name = "Chaos Mahogany Homes",
    description = "Can we build it? Yes, we can!",
    enabledByDefault = false)
@PluginDependency(Utils.class)
@Slf4j
@Extension
public class MahoganyHomes extends TickScript {

  @Inject
  private Config config;
  @Getter
  @Setter
  private Home previousHome;
  @Getter
  @Setter
  private Home currentHome;
  @Getter
  private PlankSack plankSack;
  @Getter
  @Setter
  private int lastStairsUsed;
  private boolean fixed;

  @Provides
  public Config getConfig(ConfigManager configManager) {
    return configManager.getConfig(Config.class);
  }

  @Override
  protected void onStart() {
    super.onStart();

    currentHome = null;
    plankSack = injector.getInstance(PlankSack.class);
    Static.getEventBus().register(plankSack);

    if (Inventory.contains(ItemID.PLANK_SACK)) {
      Inventory.getFirst(ItemID.PLANK_SACK).interact("Check");
    }

    addTask(GetTask.class);
    addTask(HandleBank.class);
    addTask(GoToHome.class);
    addTask(Fix.class);
    addTask(TalkToNpc.class);
  }

  @Override
  protected void onStop() {
    super.onStop();

    Static.getEventBus().unregister(plankSack);
    plankSack = null;
    currentHome = null;
  }

  public boolean hasFixed() {
    return fixed;
  }

  public void setFixed(final boolean fixed) {
    this.fixed = fixed;
  }

  public void teleport() {
    if (Utils.isInRegion(currentHome.getRegions())) {
      return;
    }

    final Item tab = Inventory.getFirst(currentHome.getTabletId());

    if (tab == null) {
      if (currentHome.getTabletId() == ItemID.TELEPORT_TO_HOUSE) {
        Interact.interactWithInventoryOrEquipment(
            ItemID.XERICS_TALISMAN,
            "Rub",
            "Xeric's Glade",
            4
        );
      } else {
        stop("Couldn't find teleport tab. Stopping plugin.");
        return;
      }
    } else {
      tab.interact("Break");
    }

    Time.sleepTicksUntil(() -> Utils.isInRegion(currentHome.getRegions()), 10);
    Time.sleepTicks(2);
  }

  public void useStairs() {
    useStairs(false);
  }

  public void useStairs(boolean ignoreLastStairs) {
    final TileObject stairs = ignoreLastStairs
        ? TileObjects.getNearest(o -> o.hasAction("Climb-up", "Climb-down")
            && o.getId() != lastStairsUsed
            && currentHome.isInHome(o))
        : TileObjects.getNearest(o -> o.hasAction("Climb-up", "Climb-down"));

    if (stairs == null) {
      return;
    }

    final int maxTries = 3;
    int tries = 0;

    while (!Reachable.isInteractable(stairs) && tries++ < maxTries) {
      ChaosMovement.openDoor(stairs);
    }

    final int plane = Players.getLocal().getWorldLocation().getPlane();

    if (stairs.hasAction("Climb-up")) {
      lastStairsUsed = stairs.getId();
      GameThread.invoke(() -> stairs.interact("Climb-up"));
    } else if (stairs.hasAction("Climb-down")) {
      GameThread.invoke(() -> stairs.interact("Climb-down"));
    }

    Time.sleepTicksUntil(() -> plane != Players.getLocal().getWorldLocation().getPlane(), 20);
  }
}
