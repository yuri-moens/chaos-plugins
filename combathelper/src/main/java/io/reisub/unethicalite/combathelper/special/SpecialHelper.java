package io.reisub.unethicalite.combathelper.special;

import io.reisub.unethicalite.combathelper.Helper;
import java.awt.event.KeyEvent;
import javax.inject.Singleton;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.Item;
import net.runelite.api.Varbits;
import net.runelite.api.events.GameTick;
import net.runelite.client.eventbus.Subscribe;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Rand;
import net.unethicalite.api.game.Combat;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.game.Vars;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.widgets.Dialog;
import net.unethicalite.client.Static;

@Singleton
public class SpecialHelper extends Helper {

  private int lastSpecial;
  private int originalWeaponId;
  private boolean shouldUseSpecial;
  private boolean shouldSwapBack;

  @Subscribe
  private void onGameTick(GameTick event) {
    if (Bank.isOpen() || Dialog.isOpen()) {
      return;
    }

    if (config.useSpecial()
        && config.activationMethod() == SpecialActivation.AUTOMATIC
        && Combat.getSpecEnergy() >= config.specialCost()
        && lastSpecial + 2 < Static.getClient().getTickCount()) {
      shouldUseSpecial = true;
    }

    if (config.useSpecial()
        && originalWeaponId != 0
        && Combat.getSpecEnergy() < config.specialCost()) {
      shouldSwapBack = true;
    }

    plugin.schedule(this::tick, Rand.nextInt(100, 150));
  }

  public void keyPressed(KeyEvent e) {
    if (config.specialHotkey().matches(e)
        && config.useSpecial()
        && config.activationMethod() == SpecialActivation.HOTKEY
        && Combat.getSpecEnergy() >= config.specialCost()
        && lastSpecial + 2 < Static.getClient().getTickCount()) {
      shouldUseSpecial = true;
      e.consume();
    }
  }

  private void tick() {
    boolean didAction = false;

    if (shouldSwapBack) {
      final Item weapon = Inventory.getFirst(originalWeaponId);

      if (weapon != null) {
        weapon.interact("Wield");
        didAction = true;
        shouldSwapBack = false;
        originalWeaponId = 0;
      }
    }

    if (shouldUseSpecial && !Combat.isSpecEnabled()) {
      if (!config.specialWeapon().equals("")
          && !Equipment.contains(Predicates.nameContains(config.specialWeapon(), false))
          && Inventory.contains(Predicates.nameContains(config.specialWeapon(), false))
          && (config.swapFromWeapon().equals("")
          || Equipment.contains(Predicates.nameContains(config.swapFromWeapon(), false)))) {
        originalWeaponId = Equipment.fromSlot(EquipmentInventorySlot.WEAPON).getId();

        final Item weapon = Inventory.getFirst(
            Predicates.nameContains(config.specialWeapon().toLowerCase(), false));

        if (weapon != null) {
          weapon.interact("Wield");
          didAction = true;
        }
      }

      if (!config.swapFromWeapon().equals("")
          && !Equipment.contains(Predicates.nameContains(config.swapFromWeapon(), false))
          && !Equipment.contains(Predicates.nameContains(config.specialWeapon(), false))) {
        shouldUseSpecial = false;
      }

      if (Vars.getBit(Varbits.PVP_SPEC_ORB) == 0 && shouldUseSpecial) {
        Combat.toggleSpec();
      }

      shouldUseSpecial = false;
      lastSpecial = Static.getClient().getTickCount();
    }

    if (didAction && plugin.getLastTarget() != null) {
      GameThread.invoke(() -> plugin.getLastTarget().interact("Attack"));
    }
  }
}
