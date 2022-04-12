package io.reisub.unethicalite.farming;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.Players;
import dev.hoot.api.entities.TileObjects;
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
import net.runelite.api.Varbits;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.widgets.Widget;

@RequiredArgsConstructor
@Getter
public enum Location {
    ARDOUGNE(
            "Ardougne",
            new WorldPoint(2670, 3376, 0),
            Varbits.FARMING_4774,
            () -> {
                return Interact.interactWithInventoryOrEquipment(Constants.ARDOUGNE_CLOAK_IDS, "Farm Teleport", "Ardougne Farm", 0);
            }
    ),
    CATHERBY(
            "Catherby",
            new WorldPoint(2813, 3465, 0),
            Varbits.FARMING_4774,
            () -> false
    ),
    FALADOR(
            "Falador",
            new WorldPoint(3058, 3310, 0),
            Varbits.FARMING_4774,
            () -> {
                return Interact.interactWithInventoryOrEquipment(Constants.EXPLORERS_RING_IDS, "Teleport", null, 0);
            }
    ),
    FARMING_GUILD(
            "Farming Guild",
            new WorldPoint(1239, 3728, 0),
            Varbits.FARMING_4775,
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
                        return true;
                    }
                }

                return false;
            }
    ),
    HARMONY_ISLAND(
            "Harmony Island",
            new WorldPoint(3790, 2839, 0),
            Varbits.FARMING_4772,
            () -> Location.tpThroughHouse(37589)
    ),
    HOSIDIUS(
            "Hosidius",
            new WorldPoint(1740, 3550, 0),
            Varbits.FARMING_4774,
            () -> {
                Item talisman = Inventory.getFirst(ItemID.XERICS_TALISMAN);

                if (talisman == null) {
                    Widget widget = Widgets.get(Regular.TELEPORT_TO_HOUSE.getWidget());
                    if (widget != null) {
                        widget.interact("Outside");
                        return true;
                    }
                } else {
                    // TODO
                }

                return false;
            }
    ),
    PORT_PHASMATYS(
            "Porty Phasmatys",
            new WorldPoint(3606, 3531, 0),
            Varbits.FARMING_4774,
            () -> {
                Item ectophial = Inventory.getFirst(ItemID.ECTOPHIAL, ItemID.ECTOPHIAL_4252);
                if (ectophial == null) {
                    return false;
                }

                ectophial.interact("Empty"); // TODO

                Time.sleepTicksUntil(() -> Inventory.contains(ItemID.ECTOPHIAL_4252), 10);
                return Time.sleepTicksUntil(() -> Inventory.contains(ItemID.ECTOPHIAL), 10);
            }
    ),
    TROLL_STRONGHOLD(
            "Troll Stronghold",
            new WorldPoint(2828, 3694, 0),
            Varbits.FARMING_4771,
            () -> Location.tpThroughHouse(33179)
    ),
    WEISS(
            "Weiss",
            new WorldPoint(2847, 3935, 0),
            Varbits.FARMING_4771,
            () -> Location.tpThroughHouse(37581)
    );

    private final String name;
    private final WorldPoint patchPoint;
    private final Varbits varbit;
    private final Teleportable teleportable;

    @Setter
    private boolean done;

    public boolean isEnabled(Config config) {
        switch(this) {
            case FARMING_GUILD:
                return config.guildHerb();
            case ARDOUGNE:
                return config.ardougneHerb();
            case CATHERBY:
                return config.catherbyHerb();
            case FALADOR:
                return config.faladorHerb();
            case PORT_PHASMATYS:
                return config.portPhasmatysHerb();
            case HOSIDIUS:
                return config.hosidiusHerb();
            case HARMONY_ISLAND:
                return config.harmonyHerb();
            case TROLL_STRONGHOLD:
                return config.trollStrongholdHerb();
            case WEISS:
                return config.weissHerb();
        }

        return false;
    }

    public interface Teleportable {
        boolean teleport();
    }

    public static boolean tpThroughHouse(int portalId) {
        Magic.cast(Regular.TELEPORT_TO_HOUSE);

        Time.sleepTicksUntil(() -> TileObjects.getNearest(portalId) != null, 10);
        Time.sleepTicks(2);

        TileObject portal = TileObjects.getNearest(portalId);
        if (portal == null) {
            return false;
        }

        int regionId = Players.getLocal().getWorldLocation().getRegionID();

        portal.interact("Enter");
        return Time.sleepTicksUntil(() -> Players.getLocal().getWorldLocation() != null && Players.getLocal().getWorldLocation().getRegionID() != regionId, 30);
    }
}
