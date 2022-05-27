package io.reisub.unethicalite.pyramidplunder.tasks;

import io.reisub.unethicalite.pyramidplunder.PyramidPlunder;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.api.Interact;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.DialogOption;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.movement.Reachable;
import net.unethicalite.api.widgets.Dialog;

public class RechargeSceptre extends Task {

  @Inject
  private PyramidPlunder plugin;

  private NPC mummy;

  @Override
  public String getStatus() {
    return "Recharging sceptre";
  }

  @Override
  public boolean validate() {
    return Utils.isInRegion(PyramidPlunder.PYRAMID_PLUNDER_REGION)
        && (mummy = NPCs.getNearest(NpcID.GUARDIAN_MUMMY)) != null
        && plugin.getSceptreCharges() == 0
        && Reachable.isInteractable(mummy);
  }

  @Override
  public void execute() {
    GameThread.invoke(() -> mummy.interact("Talk-to"));
    Time.sleepTicksUntil(Dialog::isOpen, 10);

    final DialogOption typeOption = Inventory.contains(i -> i.getName().startsWith("Stone"))
        ? DialogOption.CHAT_OPTION_TWO
        : DialogOption.CHAT_OPTION_ONE;

    Dialog.invokeDialog(
        DialogOption.CHAT_OPTION_FOUR,
        DialogOption.PLAYER_CONTINUE,
        DialogOption.NPC_CONTINUE,
        typeOption,
        DialogOption.NPC_CONTINUE
    );

    Time.sleepTicks(2);

    Interact.interactWithInventoryOrEquipment(
        Constants.PHARAOHS_SCEPTRE_IDS,
        "Check",
        null,
        -1
    );

    Time.sleepTicks(2);
  }
}
