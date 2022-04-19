package io.reisub.unethicalite.birdhouse;

import io.reisub.unethicalite.utils.Constants;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TeleportLocation {
  NOWHERE(null, 0),
  FARMING_GUILD(Constants.SKILL_NECKLACE_IDS, 6),
  EDGEVILLE(Constants.AMULET_OF_GLORY_IDS, 1),
  FEROX_ENCLAVE_DUELING_RING(Constants.DUELING_RING_IDS, 3),
  FEROX_ENCLAVE_MINIGAME_TP(null, 0),
  HOME_TELEPORT(null, 0);

  private final Set<Integer> teleportItemIds;
  private final int optionIndex;
}
