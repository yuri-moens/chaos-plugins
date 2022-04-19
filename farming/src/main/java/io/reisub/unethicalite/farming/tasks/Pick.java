package io.reisub.unethicalite.farming.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.TileObjects;
import dev.unethicalite.api.game.GameThread;
import dev.unethicalite.api.game.Vars;
import dev.unethicalite.api.items.Inventory;
import io.reisub.unethicalite.farming.Farming;
import io.reisub.unethicalite.farming.PatchImplementation;
import io.reisub.unethicalite.farming.PatchState;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.Skill;
import net.runelite.api.TileObject;
import net.runelite.api.events.StatChanged;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.timetracking.farming.CropState;

public class Pick extends Task {
  @Inject
  private Farming plugin;

  private boolean experienceReceived;

  @Override
  public String getStatus() {
    return "Picking herbs";
  }

  @Override
  public boolean validate() {
    TileObject patch = TileObjects.getNearest(Predicates.ids(Constants.HERB_PATCH_IDS));

    if (patch == null) {
      return false;
    }

    int varbit = Vars.getBit(plugin.getCurrentLocation().getVarbit());
    PatchState patchState = PatchImplementation.HERB.forVarbitValue(varbit);

    return patchState != null && patchState.getCropState() == CropState.HARVESTABLE;
  }

  @Override
  public void execute() {
    TileObject patch = TileObjects.getNearest(Predicates.ids(Constants.HERB_PATCH_IDS));
    if (patch == null) {
      return;
    }

    experienceReceived = false;
    GameThread.invoke(() -> patch.interact("Pick"));
    Time.sleepTicksUntil(() -> experienceReceived, 20);

    GameThread.invoke(() -> patch.interact("Pick"));
    Time.sleepTick();
    GameThread.invoke(() -> patch.interact("Pick"));

    Time.sleepTicksUntil(() -> Inventory.isFull() || Vars.getBit(plugin.getCurrentLocation().getVarbit()) <= 3, 100);
  }

  @Subscribe
  private void onStatChanged(StatChanged event) {
    if (plugin.isRunning() && event.getSkill() == Skill.FARMING) {
      experienceReceived = true;
    }
  }
}
