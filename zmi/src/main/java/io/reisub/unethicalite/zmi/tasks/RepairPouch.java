package io.reisub.unethicalite.zmi.tasks;

import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.DialogOption;
import net.runelite.api.ItemID;
import net.runelite.api.widgets.Widget;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.magic.Magic;
import net.unethicalite.api.magic.SpellBook;
import net.unethicalite.api.widgets.Dialog;
import net.unethicalite.api.widgets.Widgets;

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
    final Widget npcContact = Widgets.get(SpellBook.Lunar.NPC_CONTACT.getWidget());
    if (npcContact == null) {
      return;
    }

    if (npcContact.hasAction("Dark Mage")) {
      npcContact.interact("Dark Mage");
    } else {
      Magic.cast(SpellBook.Lunar.NPC_CONTACT);
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
