package io.reisub.unethicalite.barrows.tasks;

import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.items.Inventory;
import dev.unethicalite.api.widgets.Prayers;
import dev.unethicalite.managers.Static;
import io.reisub.unethicalite.barrows.Barrows;
import io.reisub.unethicalite.barrows.Brother;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.tasks.Task;

import javax.inject.Inject;

public class DrinkPrayerPotion extends Task {
    @Inject
    private Barrows plugin;

    @Override
    public String getStatus() {
        return "Drinking prayer potion";
    }

    @Override
    public boolean validate() {
        Brother current = plugin.getCurrentBrother();

        return Prayers.getPoints() == 0
                && (current == Brother.DHAROK || current == Brother.AHRIM || current == Brother.KARIL)
                && Static.getClient().getHintArrowNpc() != null
                && Players.getLocal().getWorldLocation().getPlane() == 0;
    }

    @Override
    public void execute() {
        Inventory.getFirst(Predicates.ids(Constants.PRAYER_RESTORE_POTION_IDS)).interact(0);
    }
}
