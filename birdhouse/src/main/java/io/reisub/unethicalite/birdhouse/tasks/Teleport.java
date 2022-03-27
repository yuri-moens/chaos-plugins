package io.reisub.unethicalite.birdhouse.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.Players;
import dev.hoot.api.items.Inventory;
import dev.hoot.api.magic.Ancient;
import dev.hoot.api.magic.Lunar;
import dev.hoot.api.magic.Magic;
import dev.hoot.api.magic.Necromancy;
import dev.hoot.api.magic.Regular;
import dev.hoot.api.widgets.Dialog;
import dev.hoot.api.widgets.Widgets;
import io.reisub.unethicalite.birdhouse.BirdHouse;
import io.reisub.unethicalite.birdhouse.Config;
import io.reisub.unethicalite.birdhouse.TPLocation;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.Item;
import net.runelite.api.MenuAction;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.widgets.Widget;

import javax.inject.Inject;

public class Teleport extends Task {
    @Inject
    private BirdHouse plugin;

    @Inject
    private Config config;

    @Override
    public String getStatus() {
        return "Teleporting";
    }

    @Override
    public boolean validate() {
        return config.tpLocation() != TPLocation.NOWHERE
                && !config.farmSeaweed()
                && plugin.getEmptied().size() == 4;
    }

    @Override
    public void execute() {
        WorldPoint current = Players.getLocal().getWorldLocation();

        switch (config.tpLocation()) {
            case FARMING_GUILD:
                Item tpItem = Inventory.getFirst(Predicates.ids(config.tpLocation().getTeleportItemIds()));
                if (tpItem == null) {
                    plugin.stop("Couldn't teleport, item not found. Stopping plugin.");
                    return;
                }

                tpItem.interact("Rub");
                Time.sleepTicksUntil(() -> Widgets.isVisible(Widgets.get(187, 3)), 5);

                Widget farmingGuild = Widgets.get(187, 3, 5);
                if (farmingGuild != null) {
                    farmingGuild.interact(0, MenuAction.WIDGET_TYPE_6.getId(), farmingGuild.getIndex(), farmingGuild.getId());
                }
                break;
            case EDGEVILLE:
            case FEROX_ENCLAVE_DUELING_RING:
                tpItem = Inventory.getFirst(Predicates.ids(config.tpLocation().getTeleportItemIds()));
                if (tpItem == null) {
                    plugin.stop("Couldn't teleport, item not found. Stopping plugin.");
                    return;
                }

                tpItem.interact("Rub");
                Time.sleepTicksUntil(Dialog::isViewingOptions, 5);

                Dialog.chooseOption(config.tpLocation().getOptionIndex());
                break;
            case HOME_TELEPORT:
                switch (Magic.SpellBook.getCurrent()) {
                    case LUNAR:
                        Magic.cast(Lunar.LUNAR_HOME_TELEPORT);
                        break;
                    case ANCIENT:
                        Magic.cast(Ancient.EDGEVILLE_HOME_TELEPORT);
                        break;
                    case REGULAR:
                        Magic.cast(Regular.HOME_TELEPORT);
                        break;
                    case NECROMANCY:
                        Magic.cast(Necromancy.ARCEUUS_HOME_TELEPORT);
                        break;
                }
                break;
            case FEROX_ENCLAVE_MINIGAME_TP:
                // TODO
                break;
        }

        Time.sleepTicksUntil(() -> !Players.getLocal().getWorldLocation().equals(current), 35);

        Inventory.getAll((i) -> i.hasAction("Search")).forEach((i) -> i.interact("Search"));

        plugin.stop();
    }
}
