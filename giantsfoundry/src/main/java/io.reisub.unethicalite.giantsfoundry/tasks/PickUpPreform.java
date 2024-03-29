package io.reisub.unethicalite.giantsfoundry.tasks;

import io.reisub.unethicalite.giantsfoundry.GiantsFoundry;
import io.reisub.unethicalite.giantsfoundry.GiantsFoundryHelper;
import io.reisub.unethicalite.giantsfoundry.GiantsFoundryState;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Equipment;

public class PickUpPreform extends Task {
  @Inject
  GiantsFoundryState giantsFoundryState;
  @Inject
  GiantsFoundryHelper giantsFoundryHelper;
  @Inject
  private GiantsFoundry plugin;

  @Override
  public String getStatus() {
    return "Picking up the preform";
  }

  @Override
  public boolean validate() {
    return giantsFoundryState.getGameStage() == 2 && giantsFoundryState.getOreCount() == 0;

  }

  @Override
  public void execute() {
    TileObject jig = TileObjects.getNearest("Mould jig (Poured metal)");
    if (jig == null) {
      return;
    }
    jig.interact("Pick-up");
    Time.sleepTicksUntil(() -> Equipment.contains("Preform"), 20);
  }
}