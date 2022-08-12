package io.reisub.unethicalite.mahoganyhomes.tasks;

import com.google.common.collect.Sets;
import io.reisub.unethicalite.mahoganyhomes.Config;
import io.reisub.unethicalite.mahoganyhomes.data.Home;
import io.reisub.unethicalite.mahoganyhomes.data.Hotspot;
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
    if (plugin.getCurrentHome() == Home.LARRY
        && Players.getLocal().getWorldLocation().getPlane() == 0) {
      plugin.useStairs(true);
    }

    TileObject hotspotObject = getNext();

    if (hotspotObject == null) {
      plugin.useStairs(true);

      hotspotObject = getNext();

      if (hotspotObject == null) {
        return;
      }
    }

    final TileObject finalHotspotObject = hotspotObject;
    final Hotspot hotspot = Hotspot.getByObjectId(finalHotspotObject.getId());

    final int maxTries = 5;
    int tries = 0;

    while (!Reachable.isInteractable(finalHotspotObject) && tries++ < maxTries) {
      if (plugin.getCurrentHome() == Home.NOELLA
          && Players.getLocal().getWorldLocation().getPlane() == 1) {
        plugin.useStairs(true);
        return;
      }

      Set<WorldPoint> ignoreLocations = Sets.newHashSet(
          new WorldPoint(3231, 3387, 0),
          new WorldPoint(1762, 3612, 0),
          new WorldPoint(1762, 3613, 0),
          new WorldPoint(1765, 3619, 0),
          new WorldPoint(1798, 3605, 0),
          new WorldPoint(1798, 3611, 0),
          new WorldPoint(1776, 3590, 0),
          new WorldPoint(1777, 3590, 0),
          new WorldPoint(1787, 3590, 0),
          new WorldPoint(1787, 3589, 0)
      );

      if (!ChaosMovement.openDoor(finalHotspotObject, 7, ignoreLocations)) {
        plugin.useStairs(true);
        return;
      }
    }

    if (finalHotspotObject.hasAction("Repair")) {
      finalHotspotObject.interact("Repair");
    } else {
      finalHotspotObject.interact("Remove");

      Time.sleepTicksUntil(
          () -> TileObjects.getNearest(
              o -> o.hasAction("Build") && o.getWorldLocation().distanceTo(finalHotspotObject) < 3)
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

  private TileObject getNext() {
    final List<Hotspot> brokenHotspots = Hotspot.getBrokenHotspots();
    final Home home = plugin.getCurrentHome();

    final List<TileObject> hotspotObjects = TileObjects.getAll(
        o -> {
          for (final Hotspot h : brokenHotspots) {
            if (h.getObjectIds().contains(o.getId()) && home.isInHome(o)) {
              return true;
            }
          }

          return false;
        }
    );

    for (Hotspot hotspot : home.getOrder()) {
      for (TileObject hotspotObject : hotspotObjects) {
        if (hotspot.getObjectIds().contains(hotspotObject.getId())) {
          return hotspotObject;
        }
      }
    }

    return null;
  }
}
