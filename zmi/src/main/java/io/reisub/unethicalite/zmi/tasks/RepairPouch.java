package io.reisub.unethicalite.zmi.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.items.Inventory;
import dev.unethicalite.api.magic.Lunar;
import dev.unethicalite.api.magic.Magic;
import dev.unethicalite.api.widgets.Dialog;
import dev.unethicalite.api.widgets.DialogOption;
import dev.unethicalite.api.widgets.Widgets;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.ItemID;
import net.runelite.api.widgets.Widget;

public class RepairPouch extends Task {
  @Override
  public String getStatus() {
    return "Repairing pouch";
  }

  @Override
  public boolean validate() {
    return Inventory.contains(Predicates.ids(Constants.DEGRADED_ESSENCE_POUCH_IDS))
        && Inventory.contains(ItemID.COSMIC_RUNE)
        && Players.getLocal().isIdle();
  }

  @Override
  public void execute() {
    Widget npcContact = Widgets.get(Lunar.NPC_CONTACT.getWidget());
    if (npcContact == null) {
      return;
    }

    if (npcContact.hasAction("Dark Mage")) {
      npcContact.interact("Dark Mage");
    } else {
      Magic.cast(Lunar.NPC_CONTACT);
      Time.sleepTicksUntil(() -> Widgets.isVisible(Widgets.get(75, 12)), 5);

      Widgets.get(75, 12).interact("Dark Mage");
    }

    Time.sleepTicksUntil(Dialog::canContinue, 20);

    Dialog.invokeDialog(
        DialogOption.NPC_CONTINUE,
        DialogOption.CHAT_OPTION_TWO,
        DialogOption.PLAYER_CONTINUE,
        DialogOption.NPC_CONTINUE);
  }
}
