package io.reisub.unethicalite.giantsfoundry.tasks;

import io.reisub.unethicalite.giantsfoundry.GiantsFoundry;
import io.reisub.unethicalite.giantsfoundry.GiantsFoundryState;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.DialogOption;
import net.runelite.api.NPC;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.widgets.Dialog;

public class HandIn extends Task {
  @Inject
  private GiantsFoundry plugin;

  @Inject
  private GiantsFoundryState giantsFoundryState;

  @Override
  public String getStatus() {
    return "Handing in the sword";
  }

  @Override
  public boolean validate() {
    return giantsFoundryState.getProgressAmount() == 1000;
  }

  @Override
  public int execute() {
    Time.sleepTicksUntil(() -> Players.getLocal().isIdle(), 10);
    NPC kovac = NPCs.getNearest("Kovac");
    if (kovac == null) {
      return 1;
    }

    kovac.interact("Hand-in");
    Time.sleepTicksUntil(Dialog::isOpen, 20);

    Dialog.invokeDialog(
        DialogOption.NPC_CONTINUE,
        DialogOption.PLAIN_CONTINUE,
        DialogOption.CHAT_OPTION_ONE,
        DialogOption.PLAYER_CONTINUE
    );

    return 1;
  }
}