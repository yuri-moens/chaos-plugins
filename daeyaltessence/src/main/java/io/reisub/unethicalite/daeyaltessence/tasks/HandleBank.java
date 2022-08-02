package io.reisub.unethicalite.daeyaltessence.tasks;

import io.reisub.unethicalite.daeyaltessence.Config;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.api.ChaosBank;
import io.reisub.unethicalite.utils.api.ChaosMovement;
import io.reisub.unethicalite.utils.tasks.BankTask;
import lombok.RequiredArgsConstructor;
import net.runelite.api.ItemID;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;

@RequiredArgsConstructor
public class HandleBank extends BankTask {
  private static final int DARKMEYER_REGION = 14388;
  private static final WorldPoint DARKMEYER_BANK = new WorldPoint(3604, 3366, 0);
  private static final WorldPoint BANK_DOOR = new WorldPoint(3605, 3365, 0);
  private final Config config;

  @Override
  public boolean validate() {
    return config.bankGems()
        && Inventory.isFull()
        && Inventory.contains(
            ItemID.UNCUT_DIAMOND, ItemID.UNCUT_RUBY, ItemID.UNCUT_EMERALD, ItemID.UNCUT_SAPPHIRE)
        && Inventory.contains(ItemID.DRAKANS_MEDALLION);
  }

  @Override
  public int execute() {
    Inventory.getAll((i) -> i.getName().contains("Vyre noble")).forEach((i) -> i.interact("Wear"));

    Inventory.getFirst(ItemID.DRAKANS_MEDALLION).interact("Darkmeyer");
    Time.sleepTicksUntil(
        () -> Players.getLocal().getWorldLocation().getRegionID() == DARKMEYER_REGION, 10);

    Time.sleepTicks(2);

    ChaosMovement.walkTo(DARKMEYER_BANK, 1);

    TileObject door = TileObjects.getFirstAt(BANK_DOOR, ObjectID.DOOR_39406);
    if (door != null) {
      door.interact("Open");

      Time.sleepTicksUntil(
          () -> TileObjects.getFirstAt(BANK_DOOR, ObjectID.DOOR_39406) == null, 20);
    }

    open();

    ChaosBank.depositAll(false, Predicates.ids(Constants.MINEABLE_GEM_IDS));

    return 1;
  }
}
