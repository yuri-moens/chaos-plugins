package io.reisub.unethicalite.mahoganyhomes.tasks;

import com.google.common.collect.Sets;
import io.reisub.unethicalite.mahoganyhomes.Config;
import io.reisub.unethicalite.mahoganyhomes.Home;
import io.reisub.unethicalite.mahoganyhomes.Hotspot;
import io.reisub.unethicalite.mahoganyhomes.MahoganyHomes;
import io.reisub.unethicalite.utils.api.ChaosMovement;
import io.reisub.unethicalite.utils.tasks.Task;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.widgets.Widget;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.input.Keyboard;
import net.unethicalite.api.movement.Reachable;
import net.unethicalite.api.widgets.Widgets;

public class Fix extends Task {

  @Inject
  private MahoganyHomes plugin;
  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Fixing";
  }

  @Override
  public boolean validate() {
    return plugin.getCurrentHome() != null
        && plugin.getCurrentHome().isInHome(Players.getLocal())
        && !Hotspot.isEverythingFixed();
  }

  @Override
  public void execute() {
    final List<Hotspot> brokenHotspots = Hotspot.getBrokenHotspots();
    final Home home = plugin.getCurrentHome();

    TileObject nearest = TileObjects.getNearest(
        o -> {
          for (final Hotspot h : brokenHotspots) {
            if (h.getObjectIds().contains(o.getId()) && home.isInHome(o)) {
              return true;
            }
          }

          return false;
        }
    );

    if (nearest == null) {
      plugin.useStairs(true);

      nearest = TileObjects.getNearest(
          o -> {
            for (final Hotspot h : brokenHotspots) {
              if (h.getObjectIds().contains(o.getId()) && Players.getLocal().distanceTo(o) < 10) {
                return true;
              }
            }

            return false;
          }
      );

      if (nearest == null) {
        return;
      }
    }

    final TileObject finalNearest = nearest;
    final Hotspot hotspot = Hotspot.getByObjectId(finalNearest.getId());

    final int maxTries = 5;
    int tries = 0;

    while (!Reachable.isInteractable(finalNearest) && tries++ < maxTries) {
      if (plugin.getCurrentHome() == Home.NOELLA
          && Players.getLocal().getWorldLocation().getPlane() == 1) {
        plugin.useStairs(true);
        return;
      }

      Set<WorldPoint> ignoreLocations = Sets.newHashSet(
          new WorldPoint(3231, 3387, 0),
          new WorldPoint(1762, 3612, 0)
      );

      if (!ChaosMovement.openDoor(finalNearest, ignoreLocations)) {
        plugin.useStairs(true);
        return;
      }
    }

    if (finalNearest.hasAction("Repair")) {
      finalNearest.interact("Repair");
    } else {
      finalNearest.interact("Remove");

      Time.sleepTicksUntil(
          () -> TileObjects.getNearest(
              o -> o.hasAction("Build") && o.getWorldLocation().distanceTo(finalNearest) < 3)
              != null, 15);

      final TileObject buildObject = TileObjects.getNearest(o -> o.hasAction("Build"));

      if (buildObject == null) {
        return;
      }

      buildObject.interact("Build");

      Time.sleepTicksUntil(() -> Widgets.isVisible(Widgets.get(458, 3)), 10);

      Widget buildWidget = Widgets.get(458, 6);

      if (buildWidget != null) {
        buildWidget.interact("Build");
      }

      Keyboard.type(config.plank().getChatOption());
    }

    if (hotspot == null) {
      return;
    }

    plugin.setFixed(true);

    Time.sleepTicksUntil(hotspot::isFixed, 15);
    Time.sleepTick();
  }
}
