package io.reisub.unethicalite.combathelper.prayer;

import io.reisub.unethicalite.combathelper.prayer.QuickPrayer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProtectionPrayer {
    PROTECT_FROM_MAGIC(QuickPrayer.PROTECT_FROM_MAGIC),
    PROTECT_FROM_MISSILES(QuickPrayer.PROTECT_FROM_MISSILES),
    PROTECT_FROM_MELEE(QuickPrayer.PROTECT_FROM_MELEE);

    private final QuickPrayer quickPrayer;
}
