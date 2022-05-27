package io.reisub.unethicalite.utils.tasks;

import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.api.Predicates;
import java.time.Duration;
import java.time.Instant;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.NPC;
import net.runelite.api.TileObject;
import net.runelite.api.widgets.WidgetID;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.movement.Movement;
import net.unethicalite.api.packets.DialogPackets;
import net.unethicalite.api.widgets.Widgets;
import net.unethicalite.client.Static;

public abstract class BankTask extends Task {
  protected Instant last = Instant.EPOCH;
  protected Instant lastStamina = Instant.EPOCH;

  @Override
  public String getStatus() {
    return "Banking";
  }

  protected boolean open() {
    return open(false);
  }

  protected boolean open(boolean openMainTab) {
    return open(15, openMainTab);
  }

  protected boolean open(int waitTicks) {
    return open(waitTicks, 3);
  }

  protected boolean open(int waitTicks, boolean openMainTab) {
    return open(waitTicks, 3, openMainTab);
  }

  protected boolean open(int waitTicks, int movingCheck) {
    return open(waitTicks, movingCheck, false);
  }

  protected boolean open(int waitTicks, int movingCheck, boolean openMainTab) {
    if (Bank.isOpen()) {
      if (openMainTab) {
        Bank.openMainTab();

        Time.sleepTicksUntil(Bank::isMainTabOpen, 2);
      }

      last = Instant.now();
      return true;
    }

    TileObject bankObject = getBankObject();
    NPC bankNpc;

    if (bankObject != null) {
      if (bankObject.hasAction("Bank")) {
        GameThread.invoke(() -> bankObject.interact("Bank"));
      } else if (bankObject.hasAction("Use")) {
        GameThread.invoke(() -> bankObject.interact("Use"));
      } else {
        GameThread.invoke(() -> bankObject.interact(0));
      }
    } else {
      bankNpc = getBankNpc();

      if (bankNpc == null) {
        return false;
      }

      if (bankNpc.hasAction("Bank")) {
        GameThread.invoke(() -> bankNpc.interact("Bank"));
      } else {
        GameThread.invoke(() -> bankNpc.interact(0));
      }
    }

    if (movingCheck > 0) {
      if (!Time.sleepTicksUntil(
          () -> Bank.isOpen() || Players.getLocal().isMoving(), movingCheck)) {
        return false;
      }
    }

    Time.sleepTicksUntil(Bank::isOpen, waitTicks);

    last = Instant.now();

    if (Bank.isOpen() && openMainTab) {
      Bank.openMainTab();

      Time.sleepTicksUntil(Bank::isMainTabOpen, 2);
    }

    return Bank.isOpen();
  }

  protected boolean open(String name) {
    return open(name, 15, false);
  }

  protected boolean open(String name, boolean openMainTab) {
    return open(name, 15, openMainTab);
  }

  protected boolean open(String name, int waitTicks) {
    return open(name, waitTicks, false);
  }

  protected boolean open(String name, int waitTicks, boolean openMainTab) {
    return open(name, waitTicks, 3, openMainTab);
  }

  protected boolean open(String name, int waitTicks, int movingCheck) {
    return open(name, waitTicks, movingCheck, false);
  }

  protected boolean open(String name, int waitTicks, int movingCheck, boolean openMainTab) {
    if (Bank.isOpen()) {
      if (openMainTab) {
        Bank.openMainTab();
      }

      return true;
    }

    NPC bankNpc = NPCs.getNearest(name);
    if (bankNpc == null) {
      return false;
    }

    GameThread.invoke(() -> bankNpc.interact("Bank"));

    if (movingCheck > 0) {
      if (!Time.sleepTicksUntil(
          () ->
              Bank.isOpen()
                  || Players.getLocal().isMoving()
                  || Widgets.isVisible(Widgets.get(WidgetID.BANK_PIN_GROUP_ID, 0)),
          movingCheck)) {
        return false;
      }
    }

    Time.sleepTicksUntil(Bank::isOpen, waitTicks);

    last = Instant.now();

    if (Bank.isOpen() && openMainTab && !Bank.isMainTabOpen()) {
      Bank.openMainTab();
    }

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

    potion.interact("Drink");
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

  protected boolean isBankObjectAvailable() {
    return getBankObject() != null || getBankNpc() != null;
  }

  protected TileObject getBankObject() {
    return TileObjects.getNearest(Predicates.ids(Constants.BANK_OBJECT_IDS));
  }

  protected NPC getBankNpc() {
    return NPCs.getNearest(Predicates.ids(Constants.BANK_NPC_IDS));
  }
}
