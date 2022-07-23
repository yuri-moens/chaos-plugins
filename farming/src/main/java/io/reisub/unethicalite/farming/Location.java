package io.reisub.unethicalite.farming;

import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.api.ChaosMovement;
import io.reisub.unethicalite.utils.api.Interact;
import io.reisub.unethicalite.utils.enums.HouseTeleport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;
import net.runelite.api.Varbits;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.widgets.Widget;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.magic.SpellBook;
import net.unethicalite.api.widgets.Widgets;

@RequiredArgsConstructor
@Getter
public enum Location {
  ARDOUGNE(
      "Ardougne",
      new WorldPoint(2670, 3376, 0),
      10548,
      Varbits.FARMING_4774,
      Varbits.FARMING_4773,
      () ->
          Interact.interactWithInventoryOrEquipment(
              Constants.ARDOUGNE_CLOAK_IDS, "Farm Teleport", "Ardougne Farm", 0)),
  CATHERBY(
      "Catherby",
      new WorldPoint(2813, 3465, 0),
      11062,
      Varbits.FARMING_4774,
      Varbits.FARMING_4773,
      () -> false),
  FALADOR(
      "Falador",
      new WorldPoint(3058, 3310, 0),
      12083,
      Varbits.FARMING_4774,
      Varbits.FARMING_4773,
      () ->
          Interact.interactWithInventoryOrEquipment(
              Constants.EXPLORERS_RING_IDS, "Teleport", null, 0)),
  FARMING_GUILD(
      "Farming Guild",
      new WorldPoint(1239, 3728, 0),
      4922,
      Varbits.FARMING_4775,
      Varbits.FARMING_7906,
      () -> {
        Item necklace = Inventory.getFirst(Predicates.ids(Constants.SKILL_NECKLACE_IDS));

        if (necklace == null) {
          // TODO
        } else {
          necklace.interact("Rub");
          Time.sleepTicksUntil(() -> Widgets.isVisible(Widgets.get(187, 3)), 5);

          Widget farmingGuild = Widgets.get(187, 3, 5);
          if (farmingGuild != null) {
            farmingGuild.interact(
                0,
                MenuAction.WIDGET_CONTINUE.getId(),
                farmingGuild.getIndex(),
                farmingGuild.getId());
            return true;
          }
        }

        return false;
      }),
  HARMONY_ISLAND(
      "Harmony Island",
      new WorldPoint(3790, 2839, 0),
      15148,
      Varbits.FARMING_4772,
      0,
      () -> ChaosMovement.teleportThroughHouse(HouseTeleport.HARMONY_ISLAND)),
  HOSIDIUS(
      "Hosidius",
      new WorldPoint(1740, 3550, 0),
      6967,
      Varbits.FARMING_4774,
      Varbits.FARMING_4773,
      () -> {
        Item talisman = Inventory.getFirst(ItemID.XERICS_TALISMAN);

        if (talisman == null) {
          Widget widget = Widgets.get(SpellBook.Standard.TELEPORT_TO_HOUSE.getWidget());
          if (widget != null) {
            widget.interact("Outside");
            return true;
          }
        } else {
          // TODO
        }

        return false;
      }),
  PORT_PHASMATYS(
      "Port Phasmatys",
      new WorldPoint(3606, 3531, 0),
      14391,
      Varbits.FARMING_4774,
      Varbits.FARMING_4773,
      () -> {
        Item ectophial = Inventory.getFirst(ItemID.ECTOPHIAL, ItemID.ECTOPHIAL_4252);
        if (ectophial == null) {
          return false;
        }

        ectophial.interact("Empty");

        Time.sleepTicksUntil(() -> Inventory.contains(ItemID.ECTOPHIAL_4252), 10);
        return Time.sleepTicksUntil(() -> Inventory.contains(ItemID.ECTOPHIAL), 10);
      }),
  TROLL_STRONGHOLD(
      "Troll Stronghold",
      new WorldPoint(2828, 3694, 0),
      11321,
      Varbits.FARMING_4771,
      0,
      () -> ChaosMovement.teleportThroughHouse(HouseTeleport.TROLL_STRONGHOLD)),
  WEISS(
      "Weiss",
      new WorldPoint(2847, 3935, 0),
      11325,
      Varbits.FARMING_4771,
      0,
      () -> ChaosMovement.teleportThroughHouse(HouseTeleport.WEISS, 70));

  private final String name;
  private final WorldPoint patchPoint;
  private final int regionId;
  private final int herbVarbit;
  private final int flowerVarbit;
  private final Teleportable teleportable;

  @Setter private boolean skip;

  public boolean hasFlowerPatch() {
    return flowerVarbit != 0;
  }

  public boolean isEnabled(Config config) {
    switch (this) {
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
      default:
    }

    return false;
  }

  public interface Teleportable {
    boolean teleport();
  }
}
