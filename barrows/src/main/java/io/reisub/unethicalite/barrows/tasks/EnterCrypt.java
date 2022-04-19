package io.reisub.unethicalite.barrows.tasks;

import com.openosrs.client.util.WeaponStyle;
import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.coords.RectangularArea;
import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.game.Combat;
import dev.unethicalite.api.items.Inventory;
import dev.unethicalite.api.movement.Movement;
import io.reisub.unethicalite.barrows.Barrows;
import io.reisub.unethicalite.combathelper.CombatHelper;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.api.ChaosMovement;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.runelite.api.coords.WorldPoint;

public class EnterCrypt extends Task {
  @Inject private Barrows plugin;

  @Inject private CombatHelper combatHelper;

  @Override
  public String getStatus() {
    return "Going to " + plugin.getCurrentBrother().getName();
  }

  @Override
  public boolean validate() {
    return plugin.getCurrentBrother() != null && Utils.isInRegion(Barrows.BARROWS_REGION);
  }

  @Override
  public void execute() {
    WorldPoint digAreaPoint = plugin.getCurrentBrother().getLocation().dx(-1).dy(-1);

    RectangularArea digArea = new RectangularArea(digAreaPoint, 2, 2);

    if (digArea.contains(Players.getLocal())) {
      Inventory.getFirst(ItemID.SPADE).interact(0);

      Time.sleepTicksUntil(() -> Utils.isInRegion(Barrows.CRYPT_REGION), 10);
      return;
    }

    ChaosMovement.walkTo(plugin.getCurrentBrother().getLocation(), 1);

    if (!Time.sleepTicksUntil(() -> Players.getLocal().isMoving(), 3)) {
      if (Players.getLocal().distanceTo(plugin.getCurrentBrother().getLocation()) < 8) {
        Movement.walk(plugin.getCurrentBrother().getLocation());
      }

      return;
    }

    switch (plugin.getCurrentBrother()) {
      case AHRIM:
        if (Combat.getCurrentWeaponStyle() != WeaponStyle.RANGE) {
          combatHelper.getSwapHelper().swap(true, true, WeaponStyle.RANGE);
        }
        break;
      case KARIL:
        if (Combat.getCurrentWeaponStyle() != WeaponStyle.MELEE) {
          combatHelper.getSwapHelper().swap(true, true, WeaponStyle.MELEE);
        }
        break;
      default:
        if (Combat.getCurrentWeaponStyle() != WeaponStyle.MAGIC) {
          combatHelper.getSwapHelper().swap(true, true, WeaponStyle.MAGIC);
        }
        break;
    }

    if (!Time.sleepTicksUntil(
        () -> digArea.contains(Players.getLocal()) || !Players.getLocal().isMoving(), 30)) {
      return;
    }

    Inventory.getFirst(ItemID.SPADE).interact(0);

    Time.sleepTicksUntil(() -> Utils.isInRegion(Barrows.CRYPT_REGION), 10);
  }
}
