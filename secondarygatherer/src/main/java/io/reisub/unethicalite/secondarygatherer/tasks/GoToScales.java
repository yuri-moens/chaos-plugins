package io.reisub.unethicalite.secondarygatherer.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.entities.TileObjects;
import io.reisub.unethicalite.secondarygatherer.Config;
import io.reisub.unethicalite.secondarygatherer.Secondary;
import io.reisub.unethicalite.secondarygatherer.SecondaryGatherer;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.api.ChaosMovement;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;

public class GoToScales extends Task {

  @Inject
  private SecondaryGatherer plugin;
  @Inject
  private Config config;

  public static final WorldPoint SCALES_WORLDPOINT = new WorldPoint(1929, 8969, 1);

  @Override
  public String getStatus() {
    return "Going to scales";
  }

  @Override
  public boolean validate() {
    return config.secondary() == Secondary.BLUE_DRAGON_SCALE
        && Utils.isInRegion(SecondaryGatherer.MYTHS_GUILD_DUMGEON_REGION,
        SecondaryGatherer.MYTHS_GUILD_REGION)
        && Players.getLocal().distanceTo(SCALES_WORLDPOINT) > 15;
  }

  @Override
  public void execute() {
    if (Utils.isInRegion(SecondaryGatherer.MYTHS_GUILD_REGION)) {
      final TileObject statue = TileObjects.getNearest(ObjectID.MYTHIC_STATUE);
      if (statue == null) {
        return;
      }

      statue.interact("Enter");
      Time.sleepTicksUntil(() -> Utils.isInRegion(SecondaryGatherer.MYTHS_GUILD_DUMGEON_REGION),
          10);
      Time.sleepTick();
    }

    if (!plugin.getCombatHelper().getPrayerHelper().isFlicking()) {
      plugin.getCombatHelper().getPrayerHelper().toggleFlicking();
    }

    int last = 0;

    ChaosMovement.walkTo(SCALES_WORLDPOINT);
  }
}
