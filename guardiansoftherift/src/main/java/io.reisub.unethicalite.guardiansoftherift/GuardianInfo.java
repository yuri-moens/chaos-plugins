package io.reisub.unethicalite.guardiansoftherift;

import com.google.common.collect.ImmutableSet;
import java.awt.image.BufferedImage;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import lombok.Getter;
import net.runelite.api.ItemID;
import net.runelite.client.game.ItemManager;

public class GuardianInfo {
  public static final GuardianInfo AIR = new GuardianInfo(1, ItemID.AIR_RUNE, 26887,4353, 43701 , false, CellType.Weak);
  public static final GuardianInfo MIND = new GuardianInfo(2, ItemID.MIND_RUNE, 26891,4354, 43705, true, CellType.Weak);
  public static final GuardianInfo WATER = new GuardianInfo(5, ItemID.WATER_RUNE, 26888,4355, 43702, false, CellType.Medium);
  public static final GuardianInfo EARTH = new GuardianInfo(9, ItemID.EARTH_RUNE, 26889,4356, 43703, false, CellType.Strong);
  public static final GuardianInfo FIRE = new GuardianInfo(14, ItemID.FIRE_RUNE, 26890,4357, 43704, false, CellType.Overcharged);
  public static final GuardianInfo BODY = new GuardianInfo(20, ItemID.BODY_RUNE, 26895,4358, 43709, true, CellType.Weak);
  public static final GuardianInfo COSMIC = new GuardianInfo(27, ItemID.COSMIC_RUNE, 26896, 4359, 43710, true, CellType.Medium);
  public static final GuardianInfo CHAOS = new GuardianInfo(35, ItemID.CHAOS_RUNE, 26892,4360, 43706, true, CellType.Medium);
  public static final GuardianInfo NATURE = new GuardianInfo(44, ItemID.NATURE_RUNE, 26897, 4361, 43711, true, CellType.Strong);
  public static final GuardianInfo LAW = new GuardianInfo(54, ItemID.LAW_RUNE, 26898,4362, 43712, true, CellType.Strong);
  public static final GuardianInfo DEATH = new GuardianInfo(65, ItemID.DEATH_RUNE, 26893,4363, 43707, true, CellType.Overcharged);
  public static final GuardianInfo BLOOD = new GuardianInfo(77, ItemID.BLOOD_RUNE, 26894,4364, 43708, true, CellType.Overcharged);

  public static final Set<GuardianInfo> ALL = ImmutableSet.of(AIR, MIND, WATER, EARTH, FIRE, BODY, COSMIC, CHAOS, NATURE, LAW, DEATH, BLOOD);

  @Getter
  public int levelRequired;
  public int runeId;
  public int talismanId;
  public int spriteId;
  public int objectId;
  public boolean isCatalytic;
  public CellType cellType;

  public Optional<Instant> spawnTime = Optional.empty();

  public GuardianInfo(int levelRequired, int runeId, int talismanId, int spriteId, int objectId, boolean isCatalytic, CellType cellType) {
    this.levelRequired = levelRequired;
    this.runeId = runeId;
    this.talismanId = talismanId;
    this.spriteId = spriteId;
    this.objectId = objectId;
    this.isCatalytic = isCatalytic;
    this.cellType = cellType;
  }

  public BufferedImage getRuneImage(ItemManager itemManager) {
    return itemManager.getImage(runeId);
  }

  public BufferedImage getTalismanImage(ItemManager itemManager) {
    return itemManager.getImage(talismanId);
  }

  public void spawn() {
    spawnTime = Optional.of(Instant.now());
  }

  public void despawn() {
    spawnTime = Optional.empty();
  }

  public static GuardianInfo getForObjectId(int objectId) {
    for (GuardianInfo gi : ALL) {
      if (gi.objectId == objectId) {
        return gi;
      }
    }
    return null;
  }


}