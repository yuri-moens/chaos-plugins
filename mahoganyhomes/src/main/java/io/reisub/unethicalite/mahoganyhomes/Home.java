package io.reisub.unethicalite.mahoganyhomes;

import com.google.common.collect.ImmutableSet;
import java.util.ArrayDeque;
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
  NONE(
      null,
      null,
      null,
      0,
      null,
      null,
      0,
      null,
      null
  ),
  // Ardy
  JESS(
      new WorldArea(2611, 3290, 14, 7, 0),
      new WorldPoint(2655, 3283, 0),
      "Upstairs of the building south of the church in East Ardougne",
      NpcID.JESS,
      new WorldPoint(2621, 3292, 0),
      new RequiredMaterials(12, 15, 0, 1),
      ItemID.ARDOUGNE_TELEPORT,
      ImmutableSet.of(10547, 10291, 10292),
      new ArrayDeque<>() {
        {
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_4);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_3);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_5);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_1);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_7);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_6);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_8);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_2);
        }
      },
      17026,
      16685),
  NOELLA(
      new WorldArea(2652, 3317, 15, 8, 0),
      new WorldPoint(2655, 3283, 0),
      "North of East Ardougne market",
      NpcID.NOELLA,
      new WorldPoint(2659, 3321, 0),
      new RequiredMaterials(11, 15, 0, 0),
      ItemID.ARDOUGNE_TELEPORT,
      ImmutableSet.of(10547, 10291, 10292),
      new ArrayDeque<>() {
        {
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_3);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_4);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_2);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_1);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_8);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_6);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_7);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_5);
        }
      },
      17026,
      16685,
      15645,
      15648),
  ROSS(
      new WorldArea(2609, 3313, 11, 9, 0),
      new WorldPoint(2615, 3332, 0),
      "North of the church in East Ardougne",
      NpcID.ROSS,
      new WorldPoint(2611, 3316, 0),
      new RequiredMaterials(8, 11, 0, 1),
      ItemID.ARDOUGNE_TELEPORT,
      ImmutableSet.of(10547, 10291, 10292),
      new ArrayDeque<>() {
        {
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_3);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_2);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_1);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_7);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_6);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_5);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_4);
        }
      },
      16683,
      16679),

  // Falador
  LARRY(
      new WorldArea(3033, 3360, 10, 9, 0),
      new WorldPoint(3012, 3355, 0),
      "North of the fountain in Falador",
      NpcID.LARRY_10418,
      new WorldPoint(3038, 3362, 0),
      new RequiredMaterials(9, 12, 0, 1),
      ItemID.FALADOR_TELEPORT,
      ImmutableSet.of(11828, 12084),
      new ArrayDeque<>() {
        {
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_4);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_5);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_3);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_2);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_1);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_7);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_6);
        }
      },
      24075,
      24076),
  NORMAN(
      new WorldArea(3034, 3341, 8, 8, 0),
      new WorldPoint(3012, 3355, 0),
      "South of the fountain in Falador",
      NpcID.NORMAN,
      new WorldPoint(3037, 3346, 0),
      new RequiredMaterials(9, 13, 0, 1),
      ItemID.FALADOR_TELEPORT,
      ImmutableSet.of(11828, 12084),
      new ArrayDeque<>() {
        {
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_1);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_3);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_2);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_5);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_6);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_4);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_7);
        }
      },
      24082,
      24085),
  TAU(
      new WorldArea(3043, 3340, 10, 11, 0),
      new WorldPoint(3012, 3355, 0),
      "South east of the fountain in Falador",
      NpcID.TAU,
      new WorldPoint(3047, 3347, 0),
      new RequiredMaterials(9, 13, 0, 1),
      ItemID.FALADOR_TELEPORT,
      ImmutableSet.of(11828, 12084),
      new ArrayDeque<>() {
        {
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_3);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_1);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_2);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_4);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_7);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_5);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_6);
        }
      }),

  // Hosidus
  BARBARA(
      new WorldArea(1746, 3531, 10, 11, 0),
      new WorldPoint(1748, 3598, 0),
      "South of Hosidius, near the mill",
      NpcID.BARBARA,
      new WorldPoint(1747, 3534, 0),
      new RequiredMaterials(9, 10, 0, 1),
      ItemID.TELEPORT_TO_HOUSE,
      ImmutableSet.of(6966, 6967, 6968),
      new ArrayDeque<>() {
        {
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_2);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_7);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_4);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_6);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_3);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_1);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_5);
        }
      }),
  LEELA(
      new WorldArea(1781, 3589, 9, 8, 0),
      new WorldPoint(1748, 3598, 0),
      "East of the town market in Hosidius",
      NpcID.LEELA_10423,
      new WorldPoint(1785, 3594, 0),
      new RequiredMaterials(9, 13, 0, 1),
      ItemID.TELEPORT_TO_HOUSE,
      ImmutableSet.of(6966, 6967, 6968),
      new ArrayDeque<>() {
        {
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_3);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_1);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_2);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_4);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_7);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_6);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_5);
        }
      },
      11794,
      11802),
  MARIAH(
      new WorldArea(1762, 3618, 10, 7, 0),
      new WorldPoint(1748, 3598, 0),
      "West of the estate agents in Hosidius",
      NpcID.MARIAH,
      new WorldPoint(1767, 3622, 0),
      new RequiredMaterials(11, 14, 0, 1),
      ItemID.TELEPORT_TO_HOUSE,
      ImmutableSet.of(6966, 6967, 6968),
      new ArrayDeque<>() {
        {
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_1);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_8);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_2);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_4);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_3);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_7);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_5);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_6);
        }
      },
      11794,
      11802),

  // Varrock
  BOB(
      new WorldArea(3234, 3482, 10, 10, 0),
      new WorldPoint(3253, 3420, 0),
      "North-east Varrock, opposite the church",
      NpcID.BOB_10414,
      new WorldPoint(3239, 3488, 0),
      new RequiredMaterials(13, 17, 0, 0),
      ItemID.VARROCK_TELEPORT,
      ImmutableSet.of(12852, 12853, 12854),
      new ArrayDeque<>() {
        {
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_1);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_2);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_5);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_3);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_4);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_6);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_7);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_8);
        }
      },
      11797,
      11799),
  JEFF(
      new WorldArea(3235, 3445, 10, 12, 0),
      new WorldPoint(3253, 3420, 0),
      "Middle of Varrock, west of the museum",
      NpcID.JEFF_10415,
      new WorldPoint(3241, 3451, 0),
      new RequiredMaterials(11, 16, 0, 0),
      ItemID.VARROCK_TELEPORT,
      ImmutableSet.of(12852, 12853, 12854),
      new ArrayDeque<>() {
        {
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_2);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_1);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_3);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_7);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_4);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_5);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_8);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_6);
        }
      },
      11789,
      11793),
  SARAH(
      new WorldArea(3232, 3381, 8, 7, 0),
      new WorldPoint(3253, 3420, 0),
      "Along the south wall of Varrock",
      NpcID.SARAH_10416,
      new WorldPoint(3235, 3384, 0),
      new RequiredMaterials(11, 11, 0, 1),
      ItemID.VARROCK_TELEPORT,
      ImmutableSet.of(12852, 12853, 12854),
      new ArrayDeque<>() {
        {
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_3);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_2);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_5);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_1);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_6);
          add(Hotspot.MAHOGANY_HOMES_HOTSPOT_4);
        }
      });

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
  private final WorldPoint bankLocation;
  private final String hint;
  private final int npcId;
  private final RequiredMaterials requiredMaterials;
  private final int tabletId;
  private final Set<Integer> regions;
  private final ArrayDeque<Hotspot> order;
  private final Integer[] ladders;

  Home(
      final WorldArea area,
      final WorldPoint bankLocation,
      final String hint,
      final int npcId,
      final WorldPoint location,
      final RequiredMaterials requiredMaterials,
      final int tabletId,
      final Set<Integer> regions,
      final ArrayDeque<Hotspot> order,
      final Integer... ladders) {
    this.area = area;
    this.bankLocation = bankLocation;
    this.hint = hint;
    this.npcId = npcId;
    this.location = location;
    this.requiredMaterials = requiredMaterials;
    this.tabletId = tabletId;
    this.regions = regions;
    this.order = order;
    this.ladders = ladders;
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
