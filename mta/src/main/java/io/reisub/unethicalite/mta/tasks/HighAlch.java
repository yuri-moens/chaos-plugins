package io.reisub.unethicalite.mta.tasks;

import io.reisub.unethicalite.mta.Mta;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.Item;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetID;
import net.runelite.client.plugins.mta.alchemy.AlchemyItem;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.magic.Magic;
import net.unethicalite.api.magic.SpellBook;
import net.unethicalite.api.widgets.Tab;
import net.unethicalite.api.widgets.Tabs;
import net.unethicalite.api.widgets.Widgets;
import net.unethicalite.client.Static;

public class HighAlch extends Task {
  private int last;
  private AlchemyItem best = null;

  @Override
  public String getStatus() {
    return "Casting high alchemy";
  }

  @Override
  public boolean validate() {
    best = getBest();

    return Static.getClient().getTickCount() > last + 3
        && Utils.isInRegion(Mta.MTA_REGION)
        && Players.getLocal().getWorldLocation().getPlane() == 2
        && best != null
        && (!Inventory.contains("Coins") || Inventory.getCount(true, "Coins") < 10000)
        && Inventory.contains(best.getId());
  }

  @Override
  public int execute() {
    final Item item = Inventory.getFirst(best.getId());
    if (item == null) {
      return 1;
    }

    Magic.cast(SpellBook.Standard.HIGH_LEVEL_ALCHEMY, item);

    Tabs.openInterface(Tab.INVENTORY);

    last = Static.getClient().getTickCount();
    return 1;
  }

  private AlchemyItem getBest() {
    for (int i = 0; i < 5; i++) { // 12
      final Widget textWidget = Widgets.get(WidgetID.MTA_ALCHEMY_GROUP_ID, 7 + i);
      if (textWidget == null) {
        return null;
      }

      final String item = textWidget.getText();
      final Widget pointsWidget = Widgets.get(WidgetID.MTA_ALCHEMY_GROUP_ID, 12 + i);
      final int points = Integer.parseInt(pointsWidget.getText());

      if (points == 30) {
        return AlchemyItem.find(item);
      }
    }

    return null;
  }
}
