package io.reisub.unethicalite.birdhouse.tasks;

import io.reisub.unethicalite.birdhouse.BirdHouse;
import io.reisub.unethicalite.birdhouse.Config;
import io.reisub.unethicalite.birdhouse.TeleportLocation;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.Item;
import net.runelite.api.MenuAction;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.widgets.Widget;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.magic.Magic;
import net.unethicalite.api.magic.SpellBook;
import net.unethicalite.api.widgets.Dialog;
import net.unethicalite.api.widgets.Widgets;

public class Teleport extends Task {
  @Inject private BirdHouse plugin;

  @Inject private Config config;

  @Override
  public String getStatus() {
    return "Teleporting";
  }

  @Override
  public boolean validate() {
    return config.tpLocation() != TeleportLocation.NOWHERE
        && !config.farmSeaweed()
        && plugin.getEmptied().size() == 4;
  }

  @Override
  public void execute() {
    WorldPoint current = Players.getLocal().getWorldLocation();

    switch (config.tpLocation()) {
      case FARMING_GUILD:
        Item tpItem = Inventory.getFirst(Predicates.ids(config.tpLocation().getTeleportItemIds()));
        if (tpItem == null) {
          plugin.stop("Couldn't teleport, item not found. Stopping plugin.");
          return;
        }

        tpItem.interact("Rub");
        Time.sleepTicksUntil(() -> Widgets.isVisible(Widgets.get(187, 3)), 5);

        Widget farmingGuild = Widgets.get(187, 3, 5);
        if (farmingGuild != null) {
          farmingGuild.interact(
              0, MenuAction.WIDGET_CONTINUE.getId(), farmingGuild.getIndex(), farmingGuild.getId());
        }
        break;
      case EDGEVILLE:
      case FEROX_ENCLAVE_DUELING_RING:
        tpItem = Inventory.getFirst(Predicates.ids(config.tpLocation().getTeleportItemIds()));
        if (tpItem == null) {
          plugin.stop("Couldn't teleport, item not found. Stopping plugin.");
          return;
        }

        tpItem.interact("Rub");
        Time.sleepTicksUntil(Dialog::isViewingOptions, 5);

        Dialog.chooseOption(config.tpLocation().getOptionIndex());
        break;
      case HOME_TELEPORT:
        switch (SpellBook.getCurrent()) {
          case LUNAR:
            Magic.cast(SpellBook.Lunar.LUNAR_HOME_TELEPORT);
            break;
          case ANCIENT:
            Magic.cast(SpellBook.Ancient.EDGEVILLE_HOME_TELEPORT);
            break;
          case STANDARD:
            Magic.cast(SpellBook.Standard.HOME_TELEPORT);
            break;
          case NECROMANCY:
            Magic.cast(SpellBook.Necromancy.ARCEUUS_HOME_TELEPORT);
            break;
          default:
        }
        break;
      case FEROX_ENCLAVE_MINIGAME_TP:
        // TODO
        break;
      default:
    }

    Time.sleepTicksUntil(() -> !Players.getLocal().getWorldLocation().equals(current), 35);

    Inventory.getAll((i) -> i.hasAction("Search")).forEach((i) -> i.interact("Search"));

    plugin.stop();
  }
}
