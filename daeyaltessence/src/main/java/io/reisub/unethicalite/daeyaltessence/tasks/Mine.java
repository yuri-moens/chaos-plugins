package io.reisub.unethicalite.daeyaltessence.tasks;

import io.reisub.unethicalite.daeyaltessence.DaeyaltEssence;
import io.reisub.unethicalite.daeyaltessence.data.PluginActivity;
import io.reisub.unethicalite.utils.tasks.Task;
import lombok.RequiredArgsConstructor;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;

@RequiredArgsConstructor
public class Mine extends Task {
  private final DaeyaltEssence plugin;

  @Override
  public String getStatus() {
    return "Mining";
  }

  @Override
  public boolean validate() {
    return plugin.isCurrentActivity(PluginActivity.MINING)
        && Players.getLocal().getWorldLocation().getRegionID()
            == DaeyaltEssence.ESSENCE_MINE_REGION;
  }

  @Override
  public void execute() {
    TileObject rock = TileObjects.getNearest(ObjectID.DAEYALT_ESSENCE_39095);
    if (rock == null) {
      return;
    }

    rock.interact("Mine");
    Time.sleepTicksUntil(() -> plugin.isCurrentActivity(PluginActivity.MINING), 20);
  }
}
