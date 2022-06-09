package io.reisub.unethicalite.sulliuscep.tasks;

import io.reisub.unethicalite.sulliuscep.Sulliuscep;
import io.reisub.unethicalite.sulliuscep.SulliuscepObject;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.api.ChaosMovement;
import io.reisub.unethicalite.utils.enums.PortalTeleport;
import io.reisub.unethicalite.utils.tasks.Task;
import java.util.Comparator;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;
import net.runelite.api.ObjectID;
import net.runelite.api.TileItem;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileItems;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.movement.Reachable;
import net.unethicalite.api.widgets.Widgets;

public class GoToFirst extends Task {

  @Inject
  private Sulliuscep plugin;

  @Override
  public String getStatus() {
    return "Going to first sulliuscep";
  }

  @Override
  public boolean validate() {
    return plugin.getCurrentSulliuscep() == SulliuscepObject.SULLIUSCEP_1
        && !Utils.isInRegion(Sulliuscep.SWAMP_LOWER_REGION_ID);
  }

  @Override
  public void execute() {
    if (Utils.isInRegion(Sulliuscep.SWAMP_UPPER_REGION_ID)
        && plugin.isPitFilled()) {
      final int requiredMushrooms = 9 - Inventory.getCount(ItemID.MUSHROOM);

      Inventory.getAll(ItemID.MORT_MYRE_FUNGUS).forEach(i -> i.interact("Drop"));

      if (requiredMushrooms > 0) {
        final List<TileItem> droppedMushrooms = TileItems.getAll(ItemID.MUSHROOM);

        droppedMushrooms.sort(
            Comparator.comparingDouble(i -> i.getWorldLocation().distanceTo(Players.getLocal()))
        );

        for (int i = 0; i < requiredMushrooms && i < droppedMushrooms.size(); i++) {
          final TileItem droppedMushroom = droppedMushrooms.get(i);

          if (Reachable.isInteractable(droppedMushroom)) {
            droppedMushroom.interact("Take");
            Time.sleepTicksUntil(() -> Players.getLocal().getWorldLocation()
                .equals(droppedMushroom.getWorldLocation()), 20);
            Time.sleepTick();
          } else {
            break;
          }
        }
      }

      final TileObject jumpPad = TileObjects.getNearest(ObjectID.RUBBER_CAP_MUSHROOM);

      if (jumpPad == null) {
        return;
      }

      jumpPad.interact("Jump on");
      Time.sleepTicksUntil(
          () -> Players.getLocal().getWorldLocation().equals(new WorldPoint(3685, 3756, 0)), 30);
      Time.sleepTick();
      return;
    }

    if (Utils.isInRegion(Sulliuscep.SWAMP_UPPER_REGION_ID)) {
      ChaosMovement.teleportThroughHouse(PortalTeleport.FOSSIL_ISLAND, 101);
    }

    final TileObject tree =
        TileObjects.getNearest(i -> Constants.MAGIC_MUSHTREE_IDS.contains(i.getId()));

    if (tree == null) {
      return;
    }

    GameThread.invoke(() -> tree.interact(0));
    Time.sleepTicksUntil(
        () -> Widgets.isVisible(Widgets.get(WidgetInfo.FOSSIL_MUSHROOM_TELEPORT)), 15);

    final Widget mushroomSwampWidget = Widgets.get(WidgetInfo.FOSSIL_MUSHROOM_SWAMP);

    if (!Widgets.isVisible(mushroomSwampWidget)) {
      return;
    }

    mushroomSwampWidget.interact(
        0,
        MenuAction.WIDGET_CONTINUE.getId(),
        mushroomSwampWidget.getIndex(),
        mushroomSwampWidget.getId()
    );

    Time.sleepTicksUntil(() -> Utils.isInRegion(Sulliuscep.SWAMP_LOWER_REGION_ID), 5);
    Time.sleepTicks(2);
  }
}
