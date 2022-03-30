package io.reisub.unethicalite.farming;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.Players;
import dev.hoot.api.entities.TileObjects;
import dev.hoot.api.items.Bank;
import dev.hoot.api.items.Inventory;
import dev.hoot.api.magic.Magic;
import dev.hoot.api.magic.Regular;
import dev.hoot.api.widgets.Widgets;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.api.Interact;
import io.reisub.unethicalite.utils.api.Predicates;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.widgets.Widget;

@RequiredArgsConstructor
@Getter
public enum Location {
    ARDOUGNE(
            new WorldPoint(0, 0, 0),
            () -> {
                if (!Interact.interactWithInventoryOrEquipment(Constants.ARDOUGNE_CLOAK_IDS, "Farm Teleport", "Farm Teleport", 0)) {
                    Magic.cast(Regular.ARDOUGNE_TELEPORT);
                }
            }
    ),
    CATHERBY(
            new WorldPoint(0,0,0),
            () -> {
                Magic.cast(Regular.CAMELOT_TELEPORT);
            }
    ),
    FALADOR(
            new WorldPoint(0,0,0),
            () -> {
                if (!Interact.interactWithInventoryOrEquipment(Constants.EXPLORERS_RING_IDS, "Teleport", null, 0)) {
                    Magic.cast(Regular.FALADOR_TELEPORT);
                }
            }
    ),
    FARMING_GUILD(
            new WorldPoint(0,0,0),
            () -> {
                Item necklace = Inventory.getFirst(Predicates.ids(Constants.SKILL_NECKLACE_IDS));

                if (necklace == null) {
                    // TODO
                } else {
                    necklace.interact("Rub");
                    Time.sleepTicksUntil(() -> Widgets.isVisible(Widgets.get(187, 3)), 5);

                    Widget farmingGuild = Widgets.get(187, 3, 5);
                    if (farmingGuild != null) {
                        farmingGuild.interact(0, MenuAction.WIDGET_TYPE_6.getId(), farmingGuild.getIndex(), farmingGuild.getId());
                    }
                }
            }
    ),
    HARMONY_ISLAND(
            new WorldPoint(0,0,0),
            () -> Location.tpThroughHouse(0)
    ),
    HOSIDIUS(
            new WorldPoint(0,0,0),
            () -> {

            }
    ),
    PORT_PHASMATYS(
            new WorldPoint(0,0,0),
            () -> {
                Item ectophial = Bank.Inventory.getFirst(ItemID.ECTOPHIAL, ItemID.ECTOPHIAL_4252);
                if (ectophial == null) {
                    return;
                }

                ectophial.interact("Teleport"); // TODO
            }
    ),
    TROLL_STRONGHOLD(
            new WorldPoint(0,0,0),
            () -> Location.tpThroughHouse(0)
    ),
    WEISS(
            new WorldPoint(0,0,0),
            () -> Location.tpThroughHouse(0)
    );

    private final WorldPoint patchPoint;
    private final Teleportable teleportable;

    @Setter
    private boolean done;

    public boolean isEnabled(Config config) {
        switch(this) {
            case ARDOUGNE:
                return config.ardougneHerb();
            case CATHERBY:
                return config.catherbyHerb();
            case FALADOR:
                return config.faladorHerb();
            case FARMING_GUILD:
                return config.guildHerb();
            case HARMONY_ISLAND:
                return config.harmonyHerb();
            case HOSIDIUS:
                return config.hosidiusHerb();
            case PORT_PHASMATYS:
                return config.portPhasmatysHerb();
            case TROLL_STRONGHOLD:
                return config.trollStrongholdHerb();
            case WEISS:
                return config.weissHerb();
        }

        return false;
    }

    public interface Teleportable {
        void teleport();
    }

    public static boolean tpThroughHouse(int portalId) {
        WorldPoint current = Players.getLocal().getWorldLocation();

        Magic.cast(Regular.TELEPORT_TO_HOUSE);

        Time.sleepTicksUntil(() -> !Players.getLocal().getWorldLocation().equals(current), 15);

        TileObject portal = TileObjects.getNearest(portalId);
        if (portal == null) {
            return false;
        }

        int regionId = Players.getLocal().getWorldLocation().getRegionID();

        portal.interact(0);
        return Time.sleepTicksUntil(() -> Players.getLocal().getWorldLocation().getRegionID() != regionId, 15);
    }
}
