package io.reisub.unethicalite.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.ItemID;

@AllArgsConstructor
@Getter
public enum Metal {
    BRONZE(ItemID.BRONZE_BAR),
    IRON(ItemID.IRON_BAR),
    STEEL(ItemID.STEEL_BAR),
    MITHRIL(ItemID.MITHRIL_BAR),
    ADAMANTITE(ItemID.ADAMANTITE_BAR),
    RUNITE(ItemID.RUNITE_BAR);

    private final int barId;
}
