package io.reisub.unethicalite.tempoross.tasks;

import io.reisub.unethicalite.tempoross.Tempoross;
import io.reisub.unethicalite.tempoross.data.PluginActivity;
import io.reisub.unethicalite.utils.api.Activity;
import io.reisub.unethicalite.utils.api.ChaosMovement;
import io.reisub.unethicalite.utils.tasks.Task;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.NullObjectID;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.commons.Rand;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.Combat;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;

public class Fish extends Task {
  @Inject private Tempoross plugin;

  private NPC spot;

  @Override
  public String getStatus() {
    return "Fishing";
  }

  @Override
  public boolean validate() {
    if (!plugin.isInTemporossArea() || Inventory.isFull()) {
      return false;
    }

    if (plugin.getPhase() >= 4) {
      return false;
    }

    spot = getNearestSafeSpot(NpcID.FISHING_SPOT_10569);

    if (plugin.isCurrentActivity(PluginActivity.FISHING)) {
      if (spot != null && Players.getLocal().getInteracting() != spot) {
        return true;
      }
    }

    if (plugin.isCurrentActivity(PluginActivity.COOKING) && spot != null) {
      if (plugin.getPhase() == 1
          && plugin.getEnergy() < 100
          && plugin.getRawFish() + plugin.getCookedFish() >= 19) {
        return false;
      }

      if (plugin.getPhase() == 2
          && plugin.getCookedFishRequired() > 0
          && plugin.getCookedFishRequired() != 19
          && plugin.getRawFish() + plugin.getCookedFish() >= plugin.getCookedFishRequired()) {
        return false;
      }

      return true;
    }

    return plugin.isCurrentActivity(Activity.IDLE);
  }

  @Override
  public void execute() {
    WorldPoint target = plugin.getDudiPos().dx(7).dy(16);

    if (Players.getLocal().getWorldLocation().getY() < target.getY() - 5) {
      if (plugin.getPhase() == 1 && plugin.getEnergy() < 100) {
        Time.sleep(400, 600);
      }

      ChaosMovement.walk(target, 2);

      if (!Time.sleepTicksUntil(() -> Players.getLocal().isMoving(), 3)) {
        return;
      }

      Time.sleepTicksUntil(
          () ->
              Players.getLocal().getWorldLocation().getY() >= target.getY() - Rand.nextInt(4, 6)
                  || plugin.isWaveIncoming(),
          25);
    }

    if (plugin.getPhase() >= 2) {
      Inventory.getAll(ItemID.BUCKET, ItemID.BUCKET_OF_WATER).forEach((i) -> i.interact("Drop"));
    }

    if (Combat.getSpecEnergy() == 100
        && Equipment.contains(
            ItemID.DRAGON_HARPOON,
            ItemID.DRAGON_HARPOON_OR,
            ItemID.INFERNAL_HARPOON,
            ItemID.INFERNAL_HARPOON_OR,
            ItemID.CRYSTAL_HARPOON)) {
      Combat.toggleSpec();
    }

    if (spot == null) {
      spot = getNearestSafeSpot(NpcID.FISHING_SPOT_10565);
    }

    if (spot == null) {
      return;
    }

    spot.interact(0);
    Time.sleepTicksUntil(() -> plugin.isCurrentActivity(PluginActivity.FISHING), 3);
  }

  private NPC getNearestSafeSpot(int id) {
    List<TileObject> fires =
        TileObjects.getAll(
            (o) ->
                (o.getId() == NullObjectID.NULL_41006 || o.getId() == 37582)
                    && (plugin.getIslandArea().contains(o) || plugin.getBoatArea().contains(o)));

    if (fires.size() == 0) {
      return NPCs.getNearest(id);
    } else {
      List<WorldArea> unsafeAreas = new ArrayList<>();
      for (TileObject fire : fires) {
        unsafeAreas.add(new WorldArea(fire.getWorldLocation(), 2, 2));
      }

      List<NPC> spots = NPCs.getAll((n) -> n.getId() == id && plugin.getIslandArea().contains(n));
      spots.sort(Comparator.comparingInt(o -> o.distanceTo(Players.getLocal())));

      for (NPC spot : spots) {
        if (isSpotSafe(spot, unsafeAreas)) {
          return spot;
        }
      }
    }

    return null;
  }

  private boolean isSpotSafe(NPC spot, List<WorldArea> unsafeAreas) {
    WorldPoint p = spot.getWorldLocation();

    for (WorldArea unsafeArea : unsafeAreas) {
      if (unsafeArea.contains(p.dx(1))
          || unsafeArea.contains(p.dx(-1))
          || unsafeArea.contains(p.dy(1))
          || unsafeArea.contains(p.dy(-1))) {
        return false;
      }
    }

    return true;
  }
}
