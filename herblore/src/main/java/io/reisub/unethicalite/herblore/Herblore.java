package io.reisub.unethicalite.herblore;

import com.google.inject.Provides;
import dev.unethicalite.api.game.Game;
import dev.unethicalite.api.items.Inventory;
import io.reisub.unethicalite.herblore.tasks.Clean;
import io.reisub.unethicalite.herblore.tasks.HandleBank;
import io.reisub.unethicalite.herblore.tasks.MakeCoconutMilk;
import io.reisub.unethicalite.herblore.tasks.MakePotion;
import io.reisub.unethicalite.herblore.tasks.MakeUnfinished;
import io.reisub.unethicalite.herblore.tasks.ProcessSecondary;
import io.reisub.unethicalite.herblore.tasks.TarHerbs;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.enums.Activity;
import javax.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemID;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

@PluginDescriptor(
    name = "Chaos Herblore",
    description = "You put the lime in the coconut, you drank them both up",
    enabledByDefault = false)
@PluginDependency(Utils.class)
@Extension
@Slf4j
public class Herblore extends TickScript {
  @Inject @Getter private Config config;

  @Provides
  Config provideConfig(ConfigManager configManager) {
    return configManager.getConfig(Config.class);
  }

  @Override
  protected void onStart() {
    super.onStart();

    idleCheckInventoryChange = true;

    addTask(MakeCoconutMilk.class);
    addTask(Clean.class);
    addTask(TarHerbs.class);
    addTask(MakeUnfinished.class);
    addTask(ProcessSecondary.class);
    addTask(MakePotion.class);
    addTask(HandleBank.class);
  }

  @Subscribe
  private void onItemContainerChanged(ItemContainerChanged event) {
    if (!Utils.isLoggedIn()
        || event.getItemContainer() != Game.getClient().getItemContainer(InventoryID.INVENTORY)) {
      return;
    }

    int grimyHerbs = Inventory.getCount(getGrimyHerbIds());
    int cleanHerbs = Inventory.getCount(getCleanHerbIds());
    int bases = Inventory.getCount(getBaseIds());
    int secondaries = Inventory.getCount(config.secondary().getOriginalId());
    int vials = Inventory.getCount(ItemID.VIAL);

    if (grimyHerbs == 0 && currentActivity == Activity.CLEANING_HERBS) {
      setActivity(Activity.IDLE);
    } else if (cleanHerbs == 0
        && (currentActivity == Activity.CREATING_UNFINISHED_POTIONS
            || currentActivity == Activity.TARRING_HERBS)) {
      setActivity(Activity.IDLE);
    } else if (bases == 0 && currentActivity == Activity.CREATING_POTIONS) {
      setActivity(Activity.IDLE);
    } else if (secondaries == 0 && currentActivity == Activity.PROCESSING_SECONDARIES) {
      setActivity(Activity.IDLE);
    } else if (vials == 0 && currentActivity == Activity.MAKING_COCONUT_MILK) {
      setActivity(Activity.IDLE);
    }
  }

  public Herb getHerb() {
    if (config.task() == HerbloreTask.MAKE_POTION) {
      return config.potion().getHerb();
    } else {
      return config.herb();
    }
  }

  public int[] getGrimyHerbIds() {
    Herb herb = getHerb();

    if (herb == null) {
      return new int[] {};
    } else if (herb == Herb.ALL) {
      return Herb.getAllGrimyIds();
    } else {
      return new int[] {herb.getGrimyId()};
    }
  }

  public int[] getCleanHerbIds() {
    Herb herb = getHerb();

    if (herb == null) {
      return new int[] {};
    } else if (herb == Herb.ALL) {
      return Herb.getAllCleanIds();
    } else {
      return new int[] {herb.getCleanId()};
    }
  }

  public int[] getGrimyTarHerbIds() {
    Herb herb = getHerb();

    switch (herb) {
      case ALL:
        return new int[] {
          Herb.GUAM_LEAF.getGrimyId(),
          Herb.MARRENTILL.getGrimyId(),
          Herb.TARROMIN.getGrimyId(),
          Herb.HARRALANDER.getGrimyId()
        };
      case GUAM_LEAF:
      case MARRENTILL:
      case TARROMIN:
      case HARRALANDER:
        return new int[] {herb.getGrimyId()};
      default:
        return new int[] {};
    }
  }

  public int[] getCleanTarHerbIds() {
    Herb herb = getHerb();

    switch (herb) {
      case ALL:
        return new int[] {
          Herb.GUAM_LEAF.getCleanId(),
          Herb.MARRENTILL.getCleanId(),
          Herb.TARROMIN.getCleanId(),
          Herb.HARRALANDER.getCleanId()
        };
      case GUAM_LEAF:
      case MARRENTILL:
      case TARROMIN:
      case HARRALANDER:
        return new int[] {herb.getCleanId()};
      default:
        return new int[] {};
    }
  }

  public int[] getBaseSecondaryIds() {
    if (config.secondary() == Secondary.ALL) {
      int[] ids = new int[Secondary.values().length];

      int i = 0;

      for (Secondary secondary : Secondary.values()) {
        if (secondary == Secondary.ALL) {
          continue;
        }

        ids[i++] = secondary.getOriginalId();
      }

      return ids;
    } else {
      return new int[] {config.secondary().getOriginalId()};
    }
  }

  public int[] getBaseIds() {
    Potion potion = config.potion();

    if (potion.getHerb() == null || potion.getSecondaryId() == -1) {
      return new int[] {potion.getBaseId()};
    } else {
      return potion.getHerb().getUnfinishedIds();
    }
  }

  public int[] getSecondaryIds() {
    if (config.potion().getSecondaryId() == -1) {
      return new int[] {config.potion().getHerb().getCleanId()};
    } else {
      return new int[] {config.potion().getSecondaryId()};
    }
  }
}
