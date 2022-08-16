package io.reisub.unethicalite.guardiansoftheriftsolo.tasks;

import io.reisub.unethicalite.guardiansoftheriftsolo.GuardiansOfTheRiftSolo;
import io.reisub.unethicalite.guardiansoftheriftsolo.data.AreaType;
import io.reisub.unethicalite.guardiansoftheriftsolo.data.CellType;
import io.reisub.unethicalite.guardiansoftheriftsolo.data.GuardianInfo;
import io.reisub.unethicalite.utils.tasks.Task;
import java.util.NoSuchElementException;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemID;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;

@Slf4j
@Singleton
public class EnterAltar extends Task {

  @Inject
  private GuardiansOfTheRiftSolo plugin;

  @Override
  public String getStatus() {
    return "Entering altar";
  }

  @Override
  public boolean validate() {
    if (!plugin.isGameActive()
        || plugin.getElapsedTicks() < 120 / 0.6
        || AreaType.getCurrent() != AreaType.MAIN) {
      return false;
    }

    if (Inventory.contains(ItemID.GUARDIAN_ESSENCE)
        && plugin.getEntranceTimer() <= 8
        && GuardianInfo.getBest().getCellType() == CellType.OVERCHARGED) {
      return true;
    }

    return Inventory.contains(ItemID.GUARDIAN_ESSENCE);
  }

  @Override
  public void execute() {
    try {
      final TileObject entrance = GuardianInfo.getBest().getObject();

      if (entrance == null) {
        return;
      }

      final int entranceTimer = plugin.getEntranceTimer();

      entrance.interact("Enter");
      Time.sleepTicksUntil(() -> TileObjects.getNearest("Altar") != null
          || plugin.getEntranceTimer() > entranceTimer, 16);

      if (plugin.getEntranceTimer() > entranceTimer) {
        return;
      }

      Time.sleepTick();
    } catch (NoSuchElementException e) {
      log.warn("Couldn't find an entrance");
    }
  }
}
