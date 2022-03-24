package io.reisub.unethicalite.utils.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.NPCs;
import dev.hoot.api.entities.Players;
import dev.hoot.api.entities.TileObjects;
import dev.hoot.api.items.Bank;
import dev.hoot.api.items.Equipment;
import dev.hoot.api.movement.Movement;
import dev.hoot.api.packets.DialogPackets;
import dev.hoot.bot.managers.Static;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.api.CBank;
import io.reisub.unethicalite.utils.api.Predicates;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.NPC;
import net.runelite.api.TileObject;

import java.time.Duration;
import java.time.Instant;

public abstract class BankTask extends Task {
    protected Instant last = Instant.EPOCH;
    protected Instant lastStamina = Instant.EPOCH;

    @Override
    public String getStatus() {
        return "Banking";
    }

    protected boolean open() {
        return open(15);
    }

    protected boolean open(int waitTicks) {
        return open(waitTicks, 3);
    }

    protected boolean open(int waitTicks, int movingCheck) {
        if (Bank.isOpen()) return true;

        TileObject bankObject = getBankObject();
        NPC bankNpc;

        if (bankObject != null) {
            if (bankObject.hasAction("Bank")) {
                bankObject.interact("Bank");
            } else if (bankObject.hasAction("Use")) {
                bankObject.interact("Use");
            } else {
                bankObject.interact(0);
            }
        } else {
            bankNpc = getBankNpc();

            if (bankNpc == null) return false;

            if (bankNpc.hasAction("Bank")) {
                bankNpc.interact("Bank");
            } else {
                bankNpc.interact(0);
            }
        }

        if (movingCheck > 0) {
            if (!Time.sleepTicksUntil(() -> Bank.isOpen() || Players.getLocal().isMoving(), movingCheck)) {
                return false;
            }
        }

        Time.sleepTicksUntil(Bank::isOpen, waitTicks);

        last = Instant.now();

        return Bank.isOpen();
    }

    protected boolean open(String name) {
        return open(name, 15);
    }

    protected boolean open(String name, int waitTicks) {
        return open(name, waitTicks, 3);
    }

    protected boolean open(String name, int waitTicks, int movingCheck) {
        if (Bank.isOpen()) {
            return true;
        }

        NPC bankNpc = NPCs.getNearest(name);
        if (bankNpc == null) {
            return false;
        }

        bankNpc.interact("Bank");

        if (movingCheck > 0) {
            if (!Time.sleepTicksUntil(() -> Bank.isOpen() || Players.getLocal().isMoving(), movingCheck)) {
                return false;
            }
        }

        Time.sleepTicksUntil(Bank::isOpen, waitTicks);

        last = Instant.now();

        return Bank.isOpen();
    }

    protected void close() {
        if (Bank.isOpen()) {
            DialogPackets.closeInterface();
        }
    }

    protected void drinkStamina() {
        if (!Bank.contains(Predicates.ids(Constants.STAMINA_POTION_IDS))) {
            return;
        }

        Bank.withdraw(Predicates.ids(Constants.STAMINA_POTION_IDS), 1, Bank.WithdrawMode.ITEM);

        Item potion = null;
        int start = Static.getClient().getTickCount();

        while (potion == null && Static.getClient().getTickCount() < start + 10) {
            Time.sleepTick();
            potion = Bank.Inventory.getFirst(Predicates.ids(Constants.STAMINA_POTION_IDS));
        }

        if (potion == null) {
            return;
        }

        CBank.bankInventoryInteract(potion, "Drink");
        Time.sleepTick();

        Bank.depositAll(Predicates.ids(Constants.STAMINA_POTION_IDS));

        lastStamina = Instant.now();
    }

    protected boolean isLastBankDurationAgo(Duration duration) {
        return Duration.between(last, Instant.now()).compareTo(duration) >= 0;
    }

    protected boolean isStaminaExpiring(Duration timeLeft) {
        if (!Movement.isStaminaBoosted()) {
            return true;
        }

        if (lastStamina == Instant.EPOCH) {
            return false;
        }

        int seconds = Equipment.contains(ItemID.RING_OF_ENDURANCE) ? 240 : 120;

        timeLeft = Duration.ofSeconds(seconds).minus(timeLeft);

        return Duration.between(lastStamina, Instant.now()).compareTo(timeLeft) >= 0;
    }

    protected TileObject getBankObject() {
        return TileObjects.getNearest(Predicates.ids(Constants.BANK_OBJECT_IDS));
    }

    protected NPC getBankNpc() {
        return NPCs.getNearest(Predicates.ids(Constants.BANK_NPC_IDS));
    }
}
