package io.reisub.unethicalite.barrows;

import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.game.Vars;
import io.reisub.unethicalite.utils.Utils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.runelite.api.Varbits;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;

@RequiredArgsConstructor
@Getter
public enum Brother {
    AHRIM("Ahrim", new WorldPoint(3566, 3289, 0), Varbits.BARROWS_KILLED_AHRIM, new WorldPoint(3557, 9703, 3), new WorldArea(3550,9694,11,10,3)),
    DHAROK("Dharok", new WorldPoint(3575, 3298, 0), Varbits.BARROWS_KILLED_DHAROK, new WorldPoint(3556, 9718,3), new WorldArea(3549, 9710, 11, 9,3)),
    GUTHAN("Guthan", new WorldPoint(3577, 3283, 0), Varbits.BARROWS_KILLED_GUTHAN, new WorldPoint(3534, 9704,3), new WorldArea(3533, 9699, 12, 9,3)),
    KARIL("Karil", new WorldPoint(3566, 3275, 0), Varbits.BARROWS_KILLED_KARIL, new WorldPoint(3546, 9684, 3), new WorldArea(3545, 9678, 12, 10,3)),
    TORAG("Torag", new WorldPoint(3553, 3283, 0), Varbits.BARROWS_KILLED_TORAG, new WorldPoint(3568, 9683, 3), new WorldArea(3564, 9682, 11, 10,3)),
    VERAC("Verac", new WorldPoint(3557, 3298, 0), Varbits.BARROWS_KILLED_VERAC, new WorldPoint(3578, 9706, 3), new WorldArea(3568, 9702, 11, 8,3));

    private final String name;
    private final WorldPoint location;
    private final Varbits killedVarbit;
    private final WorldPoint pointNextToStairs;
    private final WorldArea cryptArea;

    @Getter
    @Setter
    private boolean inTunnel;

    public boolean isDead() {
        return Vars.getBit(killedVarbit) > 0;
    }

    public static Brother getBrotherByCrypt() {
        if (!Utils.isInRegion(Barrows.CRYPT_REGION) || Players.getLocal().getWorldLocation().getPlane() != 3) {
            return null;
        }

        for (Brother brother : Brother.values()) {
            if (brother.cryptArea.contains(Players.getLocal())) {
                return brother;
            }
        }

        return null;
    }
}
