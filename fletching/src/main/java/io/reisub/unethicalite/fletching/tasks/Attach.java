package io.reisub.unethicalite.fletching.tasks;

import io.reisub.unethicalite.fletching.Config;
import io.reisub.unethicalite.fletching.Fletching;
import io.reisub.unethicalite.fletching.data.PluginActivity;
import io.reisub.unethicalite.fletching.data.Product;
import io.reisub.unethicalite.utils.api.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import java.util.function.Predicate;
import javax.inject.Inject;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.Skill;
import net.runelite.api.events.StatChanged;
import net.runelite.client.eventbus.Subscribe;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.widgets.Dialog;
import net.unethicalite.api.widgets.Production;
import net.unethicalite.client.Static;

public class Attach extends Task {

  @Inject
  private Fletching plugin;
  @Inject
  private Config config;
  private final Predicate<Item> featherlessPredicate = i -> i.getId() == ItemID.ARROW_SHAFT
      || i.getName().contains("dart tip")
      || i.getName().contains("bolts (unf)")
      || i.getId() == ItemID.UNFINISHED_BROAD_BOLTS;
  private final Predicate<Item> arrowheadsPredicate = i -> i.getId() == ItemID.BROAD_ARROWHEADS
      || i.getName().endsWith("arrowtips");
  private final Predicate<Item> boltsPredicate = i -> i.getName().endsWith("bolts");
  private final Predicate<Item> tipsPredicate = i -> i.getName().endsWith("bolt tips");
  private int createCount;
  private int lastTick;

  @Override
  public String getStatus() {
    return "Attaching";
  }

  @Override
  public boolean validate() {
    return plugin.isCurrentActivity(Activity.IDLE)
        && ((Inventory.contains(ItemID.FEATHER) && Inventory.contains(featherlessPredicate))
        || (Inventory.contains(arrowheadsPredicate) && Inventory.contains(ItemID.HEADLESS_ARROW))
        || (Inventory.contains(boltsPredicate) && Inventory.contains(tipsPredicate)));
  }

  @Override
  public void execute() {
    if (Dialog.isOpen()) {
      Dialog.close();
    }

    final Item feathers = Inventory.getFirst(ItemID.FEATHER);
    final Item featherless = Inventory.getFirst(featherlessPredicate);
    final Item heads = Inventory.getFirst(arrowheadsPredicate);
    final Item headless = Inventory.getFirst(ItemID.HEADLESS_ARROW);
    final Item bolts = Inventory.getFirst(boltsPredicate);
    final Item tips = Inventory.getFirst(tipsPredicate);

    if (config.product() == Product.BOLTS
        || config.product() == Product.DARTS) {
      for (int i = 0; i < 10; i++) {
        feathers.useOn(featherless);
      }
    } else {
      if (feathers != null && featherless != null) {
        feathers.useOn(featherless);
      } else if (heads != null && headless != null) {
        heads.useOn(headless);
      } else if (bolts != null && tips != null) {
        tips.useOn(bolts);
      }

      if (!Time.sleepTicksUntil(Production::isOpen, 3)) {
        return;
      }

      Production.chooseOption(1);

      if (Time.sleepTicksUntil(() -> Players.getLocal().isAnimating(), 5)) {
        plugin.setActivity(PluginActivity.ATTACHING);
      }
    }
  }

  @Subscribe
  private void onStatChanged(StatChanged event) {
    if (event.getSkill() == Skill.FLETCHING
        && plugin.isCurrentActivity(PluginActivity.ATTACHING)) {
      if (lastTick != Static.getClient().getTickCount()) {
        createCount++;
        lastTick = Static.getClient().getTickCount();

        if (createCount % 10 == 0) {
          plugin.setActivity(Activity.IDLE);
        }
      }
    }
  }
}
