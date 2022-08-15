package io.reisub.unethicalite.alchemicalhydrafighter.tasks;

import io.reisub.unethicalite.alchemicalhydrafighter.AlchemicalHydraFighter;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.unethicalite.client.Static;

public class TogglePrayer extends Task {

  @Inject
  private AlchemicalHydraFighter plugin;

  @Override
  public String getStatus() {
    return "Toggling prayer on";
  }

  @Override
  public boolean validate() {
    return Static.getClient().isInInstancedRegion()
        && !plugin.getCombatHelperPlugin().getPrayerHelper().isFlicking()
        && plugin.getHydraPlugin().getHydra() != null;
  }

  @Override
  public void execute() {
    plugin.getCombatHelperPlugin().getPrayerHelper().toggleFlicking();
  }
}
