package io.reisub.unethicalite.giantsfoundry.tasks;

import io.reisub.unethicalite.giantsfoundry.Config;
import io.reisub.unethicalite.giantsfoundry.GiantsFoundry;
import io.reisub.unethicalite.giantsfoundry.GiantsFoundryHelper;
import io.reisub.unethicalite.giantsfoundry.GiantsFoundryState;
import io.reisub.unethicalite.giantsfoundry.enums.Alloy;
import io.reisub.unethicalite.utils.tasks.Task;
import java.util.Map;
import javax.inject.Inject;
import net.runelite.api.Item;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.widgets.Dialog;
import net.unethicalite.api.widgets.Production;

public class AddToCrucible extends Task {

  @Inject
  GiantsFoundryState giantsFoundryState;
  @Inject
  GiantsFoundryHelper giantsFoundryHelper;
  @Inject
  private GiantsFoundry plugin;
  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Adding to crucible";
  }

  @Override
  public boolean validate() {
    return giantsFoundryState.getGameStage() == 1
        && giantsFoundryState.getOreCount() < 28
        && haveIngredients();

  }

  @Override
  public void execute() {
    final TileObject crucible =
        TileObjects.getNearest("Crucible (empty)", "Crucible (partially full)");
    if (crucible == null) {
      return;
    }

    final Map<String, Integer> ingredientsMap = plugin.getIngredients();

    if (ingredientsMap == null) {
      Alloy bar;

      if (Inventory.contains(config.alloy1().getBarId())) {
        bar = config.alloy1();
      } else {
        bar = config.alloy2();
      }
      crucible.interact("Fill");
      Time.sleepTicksUntil(Production::isOpen, 10);
      Production.chooseOption(bar.getDialogIndex());
    } else {
      final Item ingredient = Inventory.getFirst(Predicates.names(ingredientsMap.keySet()));

      if (ingredient == null) {
        return;
      }

      GameThread.invoke(() -> ingredient.useOn(crucible));
      if (Inventory.getCount(ingredient.getId()) > 1) {
        Time.sleepTicksUntil(Dialog::isViewingOptions, 15);
        Dialog.chooseOption(3);
      }

      Time.sleepTicks(2);
    }

    Time.sleepTicksUntil(() -> Players.getLocal().isIdle(), 10);
  }

  private boolean haveIngredients() {
    final Map<String, Integer> ingredients = plugin.getIngredients();

    if (ingredients == null) {
      return Inventory.contains(config.alloy1().getBarId(), config.alloy2().getBarId());
    } else {
      return Inventory.contains(Predicates.names(ingredients.keySet()));
    }
  }
}