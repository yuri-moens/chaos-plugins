package io.reisub.unethicalite.daeyaltessence.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.Players;
import dev.hoot.api.entities.TileObjects;
import dev.hoot.api.game.Game;
import dev.hoot.api.items.Bank;
import dev.hoot.api.items.Inventory;
import dev.hoot.api.movement.Movement;
import io.reisub.unethicalite.daeyaltessence.Config;
import io.reisub.unethicalite.utils.tasks.BankTask;
import lombok.RequiredArgsConstructor;
import net.runelite.api.ItemID;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;

@RequiredArgsConstructor
public class HandleBank extends BankTask {
    private final Config config;

    private static final int DARKMEYER_REGION = 14388;
    private static final WorldPoint DARKMEYER_BANK = new WorldPoint(3604, 3366, 0);

    @Override
    public boolean validate() {
        return config.bankGems()
                && Inventory.isFull()
                && Inventory.contains(ItemID.UNCUT_DIAMOND, ItemID.UNCUT_RUBY, ItemID.UNCUT_EMERALD, ItemID.UNCUT_SAPPHIRE)
                && Inventory.contains(ItemID.DRAKANS_MEDALLION);
    }

    @Override
    public void execute() {
        Inventory.getAll((i) -> i.getName().contains("Vyre noble")).forEach((i) -> i.interact("Wear"));

        Inventory.getFirst(ItemID.DRAKANS_MEDALLION).interact("Darkmeyer");
        Time.sleepTicksUntil(() -> Players.getLocal().getWorldLocation().getRegionID() == DARKMEYER_REGION, 10);

        Time.sleepTicks(2);

        int start = Game.getClient().getTickCount();

        while (Players.getLocal().distanceTo(DARKMEYER_BANK) > 10 && Game.getClient().getTickCount() <= start + 100) {
            if (!Movement.isWalking()) {
                Movement.walkTo(DARKMEYER_BANK, 1);

                if (!Players.getLocal().isMoving()) {
                    Time.sleepTick();
                }
            }

            Time.sleepTick();
        }

        TileObject door = TileObjects.getFirstAt(3605, 3365, 0, ObjectID.DOOR_39406);
        if (door != null) {
            door.interact("Open");

            Time.sleepTicksUntil(() -> TileObjects.getFirstAt(3605, 3365, 0, ObjectID.DOOR_39406) == null, 20);
        }

        open();

        Bank.depositAll(ItemID.UNCUT_DIAMOND, ItemID.UNCUT_RUBY, ItemID.UNCUT_EMERALD, ItemID.UNCUT_SAPPHIRE);
    }
}
