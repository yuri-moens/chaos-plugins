package io.reisub.unethicalite.runecrafting.tasks;

import io.reisub.unethicalite.runecrafting.Config;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.api.ChaosMovement;
import io.reisub.unethicalite.utils.api.Interact;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemID;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.magic.SpellBook.Standard;
import net.unethicalite.api.movement.Reachable;

@Slf4j
public class GoToAltar extends Task {

  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Going to altar";
  }

  @Override
  public boolean validate() {
    return Inventory.contains(Predicates.ids(Constants.ESSENCE_IDS))
        && TileObjects.getNearest("Altar") == null;
  }

  @Override
  public void execute() {
    switch (config.rune()) {
      case TRUE_BLOOD:
        goToTrueBloodAltar();
        break;
      default:
        ChaosMovement.walkTo(config.rune().getAltarPoint());
    }
  }

  private void goToTrueBloodAltar() {
    if (!Utils.isInRegion(13721, 13977, 14232)) {
      goThroughFairyRing();
      Time.sleepTick();
    }

    enterObject(new WorldPoint(3447, 9821, 0), new WorldPoint(3460, 9813, 0));
    Time.sleepTick();
    enterObject(new WorldPoint(3467, 9820, 0), new WorldPoint(3481, 9824, 0));
    Time.sleepTick();
    enterObject(new WorldPoint(3500, 9803, 0), new WorldPoint(3536, 9768, 0));
    Time.sleepTick();
    enterObject(new WorldPoint(3540, 9772, 0), new WorldPoint(3543, 9772, 0));

    final TileObject ruins = TileObjects.getNearest("Mysterious ruins");

    if (ruins == null) {
      return;
    }

    ruins.interact("Enter");

    Time.sleepTicksUntil(() -> TileObjects.getNearest("Altar") != null, 20);
  }

  private void goThroughFairyRing() {
    TileObject fairyRing = TileObjects.getNearest("Fairy ring", "Spiritual Fairy Tree");

    if (fairyRing == null) {
      if (!goToFairyRing()) {
        return;
      }
    }

    fairyRing = TileObjects.getNearest("Fairy ring", "Spiritual Fairy Tree");

    if (fairyRing == null || !Reachable.isInteractable(fairyRing)) {
      return;
    }

    fairyRing.interact(
        "Last-destination (DLS)",
        "Ring-last-destination (DLS)"
    );

    Time.sleepTicksUntil(
        () -> Players.getLocal().getWorldLocation().equals(new WorldPoint(3447, 9824, 0)), 20);
  }

  private boolean goToFairyRing() {
    final WorldPoint current = Players.getLocal().getWorldLocation();

    if (Inventory.contains(Predicates.nameContains("Quest point cape"))
        || Equipment.contains(Predicates.nameContains("Quest point cape"))) {
      Interact.interactWithInventoryOrEquipment(
          Predicates.nameContains("Quest point cape"),
          "Teleport",
          null,
          -1
      );

      Time.sleepTicksUntil(() -> !Players.getLocal().getWorldLocation().equals(current), 10);
      Time.sleepTick();
    } else if (Inventory.contains(Predicates.nameContains("Construct. cape"))
        || Equipment.contains(Predicates.nameContains("Construct. cape"))
        || Standard.TELEPORT_TO_HOUSE.canCast()
        || Inventory.contains(ItemID.TELEPORT_TO_HOUSE)) {
      ChaosMovement.teleportToHouse();
      Time.sleepTicks(2);
      ChaosMovement.drinkFromPool(30);
    }

    return Time.sleepTicksUntil(
        () -> TileObjects.getNearest("Fairy ring", "Spiritual Fairy Tree") != null, 5);
  }

  private void enterObject(final WorldPoint objectPoint, final WorldPoint expectedEndPoint) {
    final TileObject object =
        TileObjects.getFirstAt(objectPoint, o -> o.hasAction("Enter"));

    if (object == null || !Reachable.isInteractable(object)) {
      return;
    }

    object.interact("Enter");

    Time.sleepTicksUntil(
        () -> Players.getLocal().getWorldLocation().equals(expectedEndPoint), 30);
  }
}
