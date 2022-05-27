package io.reisub.unethicalite.birdhouse.tasks;

import io.reisub.unethicalite.birdhouse.BirdHouse;
import io.reisub.unethicalite.birdhouse.Config;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.api.ChaosMovement;
import io.reisub.unethicalite.utils.tasks.Task;
import lombok.AllArgsConstructor;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.widgets.Dialog;

@AllArgsConstructor
public class GoToIsland extends Task {
  private final BirdHouse plugin;
  private final Config config;
  private final WorldPoint target = new WorldPoint(3731, 3892, 0);

  @Override
  public String getStatus() {
    return "Going to island";
  }

  @Override
  public boolean validate() {
    for (int space : Constants.BIRD_HOUSE_SPACES) {
      if (!plugin.isEmptied(space)) {
        return false;
      }
    }

    return config.farmSeaweed()
        && !plugin.isUnderwater()
        && Players.getLocal().distanceTo(BirdHouse.ISLAND) > 10;
  }

  @Override
  public void execute() {
    ChaosMovement.walkTo(
        target,
        2,
        () -> Inventory.getAll((i) -> i.hasAction("Search")).forEach((i) -> i.interact("Search")));

    TileObject rowBoat = TileObjects.getNearest(ObjectID.ROWBOAT_30915);
    if (rowBoat == null) {
      return;
    }

    while (Players.getLocal().distanceTo(BirdHouse.ISLAND) > 10) {
      GameThread.invoke(() -> rowBoat.interact("Travel"));
      Time.sleepTicksUntil(Dialog::isViewingOptions, 15);

      Dialog.chooseOption(3);
      Time.sleepTicksUntil(() -> Players.getLocal().distanceTo(BirdHouse.ISLAND) < 10, 5);
    }

    Time.sleepTick();
  }
}
