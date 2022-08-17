package io.reisub.unethicalite.guardiansoftheriftsolo.tasks;

import io.reisub.unethicalite.guardiansoftheriftsolo.GuardiansOfTheRiftSolo;
import io.reisub.unethicalite.guardiansoftheriftsolo.data.AreaType;
import io.reisub.unethicalite.guardiansoftheriftsolo.data.GuardianInfo;
import io.reisub.unethicalite.utils.tasks.Task;
import java.util.NoSuchElementException;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemID;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;

@Slf4j
public class EnterAltar extends Task {

  @Inject
  private GuardiansOfTheRiftSolo plugin;

  @Override
  public String getStatus() {
    return "Entering altar";
  }

  @Override
  public boolean validate() {
    return plugin.isGameActive()
        && AreaType.getCurrent() == AreaType.MAIN
        && Inventory.contains(ItemID.GUARDIAN_ESSENCE);
  }

  @Override
  public void execute() {
    try {
      final TileObject entrance = GuardianInfo.getBest().getObject();

      if (entrance == null) {
        return;
      }

      entrance.interact("Enter");
      Time.sleepTicksUntil(() -> TileObjects.getNearest("Altar") != null
          || !GuardianInfo.getBest().getObject().equals(entrance), 16);
    } catch (NoSuchElementException e) {
      log.warn("Couldn't find an entrance");
    }
  }
}
