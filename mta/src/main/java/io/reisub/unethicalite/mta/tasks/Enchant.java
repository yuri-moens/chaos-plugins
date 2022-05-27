package io.reisub.unethicalite.mta.tasks;

import com.google.common.collect.ImmutableSet;
import io.reisub.unethicalite.mta.Mta;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.tasks.Task;
import java.util.Set;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.magic.Magic;
import net.unethicalite.api.magic.SpellBook;
import net.unethicalite.client.Static;

public class Enchant extends Task {
  private static final Set<Integer> ENCHANT_ITEM_IDS =
      ImmutableSet.of(
          ItemID.PENTAMID,
          ItemID.CUBE,
          ItemID.CYLINDER,
          ItemID.ICOSAHEDRON,
          ItemID.DRAGONSTONE_6903);

  private int last;

  @Override
  public String getStatus() {
    return "Casting enchant";
  }

  @Override
  public boolean validate() {
    return Static.getClient().getTickCount() > last + 3
        && Utils.isInRegion(Mta.MTA_REGION)
        && Players.getLocal().getWorldLocation().getPlane() == 0
        && Inventory.contains(Predicates.ids(ENCHANT_ITEM_IDS));
  }

  @Override
  public void execute() {
    final Item item = Inventory.getFirst(Predicates.ids(ENCHANT_ITEM_IDS));
    if (item == null) {
      return;
    }

    GameThread.invoke(() -> Magic.cast(SpellBook.Standard.LVL_6_ENCHANT, item));

    last = Static.getClient().getTickCount();
  }
}
