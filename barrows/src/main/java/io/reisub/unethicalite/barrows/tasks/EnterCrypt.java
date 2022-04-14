package io.reisub.unethicalite.barrows.tasks;

import com.openosrs.client.util.WeaponStyle;
import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.Players;
import dev.hoot.api.game.Combat;
import dev.hoot.api.items.Inventory;
import io.reisub.unethicalite.barrows.Barrows;
import io.reisub.unethicalite.combathelper.CombatHelper;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.api.CMovement;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.ItemID;
import net.runelite.api.coords.WorldArea;

import javax.inject.Inject;

public class EnterCrypt extends Task {
    @Inject
    private Barrows plugin;

    @Inject
    private CombatHelper combatHelper;

    @Override
    public String getStatus() {
        return "Going to " + plugin.getCurrentBrother().getName();
    }

    @Override
    public boolean validate() {
        return plugin.getCurrentBrother() != null
                && Utils.isInRegion(Barrows.BARROWS_REGION);
    }

    @Override
    public void execute() {
        WorldArea digArea = plugin.getCurrentBrother().getLocation()
                .dx(-1)
                .dy(-1)
                .createWorldArea(3, 3);

        if (digArea.contains(Players.getLocal())) {
            Inventory.getFirst(ItemID.SPADE).interact(0);

            Time.sleepTicksUntil(() -> Utils.isInRegion(Barrows.CRYPT_REGION), 10);
            return;
        }

        CMovement.walkTo(plugin.getCurrentBrother().getLocation(), 1);

        if (!Time.sleepTicksUntil(() -> Players.getLocal().isMoving(), 3)) {
            return;
        }

        switch (plugin.getCurrentBrother()) {
            case DHAROK:
            case TORAG:
            case VERAC:
            case GUTHAN:
                if (Combat.getCurrentWeaponStyle() != WeaponStyle.MAGIC) {
                    combatHelper.getSwapHelper().swap(true, true, WeaponStyle.MAGIC);
                }
                break;
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
        }

        Time.sleepTicksUntil(() -> digArea.contains(Players.getLocal()), 30);

        Inventory.getFirst(ItemID.SPADE).interact(0);

        Time.sleepTicksUntil(() -> Utils.isInRegion(Barrows.CRYPT_REGION), 10);
    }
}
