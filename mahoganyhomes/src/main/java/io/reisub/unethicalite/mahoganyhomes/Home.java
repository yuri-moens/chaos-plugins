package io.reisub.unethicalite.mahoganyhomes;

import com.google.common.collect.ImmutableSet;
import dev.unethicalite.api.movement.pathfinder.BankLocation;
import java.util.Set;
import lombok.Getter;
import net.runelite.api.ItemID;
import net.runelite.api.Locatable;
import net.runelite.api.NpcID;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import org.apache.commons.text.WordUtils;

@Getter
public enum Home {
  // area is based on bounds of house not area at which stuff loads in for the homes
  // Ardy
  JESS(
      new WorldArea(2611, 3290, 14, 7, 0),
      BankLocation.ARDOUGNE_SOUTH_BANK,
      "Upstairs of the building south of the church in East Ardougne",
      NpcID.JESS,
      new WorldPoint(2621, 3292, 0),
      new RequiredMaterials(12, 15, 0, 1),
      ItemID.ARDOUGNE_TELEPORT,
      ImmutableSet.of(10547, 10291),
      17026,
      16685),
  NOELLA(
      new WorldArea(2652, 3317, 15, 8, 0),
      BankLocation.ARDOUGNE_SOUTH_BANK,
      "North of East Ardougne market",
      NpcID.NOELLA,
      new WorldPoint(2659, 3321, 0),
      new RequiredMaterials(11, 15, 0, 0),
      ItemID.ARDOUGNE_TELEPORT,
      ImmutableSet.of(10547, 10291),
      17026,
      16685,
      15645,
      15648),
  ROSS(
      new WorldArea(2609, 3313, 11, 9, 0),
      BankLocation.ARDOUGNE_NORTH_BANK,
      "North of the church in East Ardougne",
      NpcID.ROSS,
      new WorldPoint(2611, 3316, 0),
      new RequiredMaterials(8, 11, 0, 1),
      ItemID.ARDOUGNE_TELEPORT,
      ImmutableSet.of(10547, 10291),
      16683,
      16679),

  // Falador
  LARRY(
      new WorldArea(3033, 3360, 10, 9, 0),
      BankLocation.FALADOR_EAST_BANK,
      "North of the fountain in Falador",
      NpcID.LARRY_10418,
      new WorldPoint(3038, 3362, 0),
      new RequiredMaterials(9, 12, 0, 1),
      ItemID.FALADOR_TELEPORT,
      ImmutableSet.of(11828, 12084),
      24075,
      24076),
  NORMAN(
      new WorldArea(3034, 3341, 8, 8, 0),
      BankLocation.FALADOR_EAST_BANK,
      "South of the fountain in Falador",
      NpcID.NORMAN,
      new WorldPoint(3037, 3346, 0),
      new RequiredMaterials(9, 13, 0, 1),
      ItemID.FALADOR_TELEPORT,
      ImmutableSet.of(11828, 12084),
      24082,
      24085),
  TAU(
      new WorldArea(3043, 3340, 10, 11, 0),
      BankLocation.FALADOR_EAST_BANK,
      "South east of the fountain in Falador",
      NpcID.TAU,
      new WorldPoint(3047, 3347, 0),
      new RequiredMaterials(9, 13, 0, 1),
      ItemID.FALADOR_TELEPORT,
      ImmutableSet.of(11828, 12084)),

  // Hosidus
  BARBARA(
      new WorldArea(1746, 3531, 10, 11, 0),
      BankLocation.HOSIDIUS_BANK,
      "South of Hosidius, near the mill",
      NpcID.BARBARA,
      new WorldPoint(1748, 3534, 0),
      new RequiredMaterials(9, 10, 0, 1),
      ItemID.TELEPORT_TO_HOUSE,
      ImmutableSet.of(6967, 6968)),
  LEELA(
      new WorldArea(1781, 3589, 9, 8, 0),
      BankLocation.HOSIDIUS_BANK,
      "East of the town market in Hosidius",
      NpcID.LEELA_10423,
      new WorldPoint(1785, 3592, 0),
      new RequiredMaterials(9, 13, 0, 1),
      ItemID.TELEPORT_TO_HOUSE,
      ImmutableSet.of(6967, 6968),
      11794,
      11802),
  MARIAH(
      new WorldArea(1762, 3618, 10, 7, 0),
      BankLocation.HOSIDIUS_BANK,
      "West of the estate agents in Hosidius",
      NpcID.MARIAH,
      new WorldPoint(1765, 3623, 0),
      new RequiredMaterials(11, 14, 0, 1),
      ItemID.TELEPORT_TO_HOUSE,
      ImmutableSet.of(6967, 6968),
      11794,
      11802),

  // Varrock
  BOB(
      new WorldArea(3234, 3482, 10, 10, 0),
      BankLocation.VARROCK_EAST_BANK,
      "North-east Varrock, opposite the church",
      NpcID.BOB_10414,
      new WorldPoint(3238, 3486, 0),
      new RequiredMaterials(13, 17, 0, 0),
      ItemID.VARROCK_TELEPORT,
      ImmutableSet.of(12852, 12853, 12854),
      11797,
      11799),
  JEFF(
      new WorldArea(3235, 3445, 10, 12, 0),
      BankLocation.VARROCK_EAST_BANK,
      "Middle of Varrock, west of the museum",
      NpcID.JEFF_10415,
      new WorldPoint(3241, 3451, 0),
      new RequiredMaterials(11, 16, 0, 0),
      ItemID.VARROCK_TELEPORT,
      ImmutableSet.of(12852, 12853, 12854),
      11789,
      11793),
  SARAH(
      new WorldArea(3232, 3381, 8, 7, 0),
      BankLocation.VARROCK_EAST_BANK,
      "Along the south wall of Varrock",
      NpcID.SARAH_10416,
      new WorldPoint(3235, 3384, 0),
      new RequiredMaterials(11, 11, 0, 1),
      ItemID.VARROCK_TELEPORT,
      ImmutableSet.of(12852, 12853, 12854));

  private static final ImmutableSet<Integer> LADDERS;

  static {
    final ImmutableSet.Builder<Integer> b = new ImmutableSet.Builder<>();

    for (final Home h : values()) {
      b.add(h.getLadders());
    }

    LADDERS = b.build();
  }

  private final WorldPoint location;
  private final WorldArea area;
  private final BankLocation bankLocation;
  private final String hint;
  private final int npcId;
  private final RequiredMaterials requiredMaterials;
  private final int tabletId;
  private final Set<Integer> regions;
  private final Integer[] ladders;

  Home(
      final WorldArea area,
      final BankLocation bankLocation,
      final String hint,
      final int npcId,
      final WorldPoint location,
      final RequiredMaterials requiredMaterials,
      final int tabletId,
      final Set<Integer> regions,
      final Integer... ladders) {
    this.area = area;
    this.bankLocation = bankLocation;
    this.hint = hint;
    this.npcId = npcId;
    this.location = location;
    this.requiredMaterials = requiredMaterials;
    this.tabletId = tabletId;
    this.regions = regions;
    this.ladders = ladders;
  }

  public static boolean isLadder(final int objId) {
    return LADDERS.contains(objId);
  }

  public String getName() {
    return WordUtils.capitalize(name().toLowerCase());
  }

  public boolean isInHome(Locatable locatable) {
    for (int i = 0; i < 2; i++) {
      final WorldArea planeArea = new WorldArea(
          area.getX(),
          area.getY(),
          area.getWidth(),
          area.getHeight(),
          i
      );

      if (planeArea.contains(locatable)) {
        return true;
      }
    }

    return false;
  }
}
