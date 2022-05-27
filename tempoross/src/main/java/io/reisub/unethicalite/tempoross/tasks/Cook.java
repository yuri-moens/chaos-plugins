package io.reisub.unethicalite.tempoross.tasks;

import io.reisub.unethicalite.tempoross.Tempoross;
import io.reisub.unethicalite.utils.api.ChaosMovement;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.commons.Rand;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.Game;
import net.unethicalite.api.items.Inventory;

public class Cook extends Task {
  @Inject private Tempoross plugin;

  @Override
  public String getStatus() {
    return "Cooking";
  }

  @Override
  public boolean validate() {
    if (!plugin.isInTemporossArea()) {
      return false;
    }

    if (plugin.getRawFish() == 0) {
      return false;
    }

    if (plugin.getCurrentActivity() == Activity.ATTACKING) {
      return false;
    }

    if (plugin.getPhase() == 1
        && plugin.getEnergy() < 100
        && plugin.getRawFish() + plugin.getCookedFish() >= 19
        && plugin.getCurrentActivity() == Activity.FISHING
        && (93 - plugin.getStormIntensity())
            > plugin.getCookedFishRequired() - plugin.getCookedFish()) {
      return false;
    }

    if (plugin.getPhase() == 1
        && plugin.getEnergy() < 100
        && plugin.getRawFish() + plugin.getCookedFish() >= 19
        && (plugin.getCurrentActivity() == Activity.FISHING
            || plugin.getCurrentActivity() == Activity.IDLE)) {
      return true;
    }

    if (plugin.getPhase() == 2
        && plugin.getCookedFishRequired() > 0
        && plugin.getCookedFishRequired() != 19
        && plugin.getRawFish() + plugin.getCookedFish() >= plugin.getCookedFishRequired()
        && (plugin.getCurrentActivity() == Activity.FISHING
            || plugin.getCurrentActivity() == Activity.IDLE)) {
      return true;
    }

    NPC doubleSpot =
        NPCs.getNearest(
            (n) -> n.getId() == NpcID.FISHING_SPOT_10569 && plugin.getIslandArea().contains(n));

    if (doubleSpot != null
        && !Inventory.isFull()
        && !(plugin.getPhase() == 1
            && plugin.getCookedFishRequired() == 19
            && plugin.getRawFish() + plugin.getCookedFish() >= 19)) {
      return false;
    }

    if (plugin.getCurrentActivity() == Activity.FISHING
        && Inventory.getCount(ItemID.RAW_HARPOONFISH) >= 9
        && doubleSpot == null) {
      return true;
    }

    return plugin.getCurrentActivity() == Activity.IDLE;
  }

  @Override
  public void execute() {
    WorldPoint target = plugin.getDudiPos().dx(7).dy(16);

    if (Players.getLocal().getWorldLocation().getY() < target.getY() - 5) {
      ChaosMovement.sendMovementPacket(target.dx(Rand.nextInt(-2, 3)).dy(Rand.nextInt(-2, 3)));

      if (!Time.sleepUntil(() -> Players.getLocal().isMoving(), 1500)) {
        return;
      }

      Time.sleepUntil(
          () ->
              Players.getLocal().getWorldLocation().getY() >= target.getY() - Rand.nextInt(4, 6)
                  || plugin.isWaveIncoming(),
          10000);
    }

    NPC doubleSpot =
        NPCs.getNearest(
            (n) -> n.getId() == NpcID.FISHING_SPOT_10569 && plugin.getIslandArea().contains(n));

    if (doubleSpot != null
        && !Inventory.isFull()
        && !(plugin.getPhase() == 1
            && plugin.getCookedFishRequired() == 19
            && plugin.getRawFish() + plugin.getCookedFish() >= 19)) {
      return;
    }

    if (plugin.isWaveIncoming()) {
      return;
    }

    TileObject shrine = TileObjects.getNearest(ObjectID.SHRINE_41236);
    if (shrine == null) {
      return;
    }

    shrine.interact(0);
    Time.sleepUntil(
        () ->
            plugin.getCurrentActivity() == Activity.COOKING
                || plugin.isWaveIncoming()
                || plugin.getLastDoubleSpawn() + 3 >= Game.getClient().getTickCount(),
        10000);
  }
}
