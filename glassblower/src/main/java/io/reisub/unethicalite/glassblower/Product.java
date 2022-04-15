package io.reisub.unethicalite.glassblower;

import dev.unethicalite.api.game.Skills;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.Skill;

@AllArgsConstructor
@Getter
public enum Product {
    HIGHEST_POSSIBLE(0, 0),
    BEER_GLASS(1, 1),
    EMPTY_CANDLE_LANTERN(2, 4),
    EMPTY_OIL_LAMP(3, 12),
    VIAL(4, 33),
    FISHBOWL(5, 42),
    UNPOWERED_ORB(6, 46),
    LANTERN_LENS(7, 49),
    LIGHT_ORB(8, 87);

    private final int option;
    private final int requiredLevel;

    public static Product getHighest() {
        int craftingLevel = Skills.getBoostedLevel(Skill.CRAFTING);

        for (int i = values().length - 1; i >= 0; i--) {
            if (craftingLevel >= values()[i].requiredLevel) {
                return values()[i];
            }
        }

        return null;
    }
}
