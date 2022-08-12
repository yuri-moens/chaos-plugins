package io.reisub.unethicalite.mahoganyhomes.tasks;

import io.reisub.unethicalite.mahoganyhomes.data.Hotspot;
import io.reisub.unethicalite.mahoganyhomes.MahoganyHomes;
import io.reisub.unethicalite.utils.api.ChaosMovement;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.DialogOption;
import net.runelite.api.NPC;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.movement.Reachable;
import net.unethicalite.api.widgets.Dialog;

public class TalkToNpc extends Task {

  @Inject
  private MahoganyHomes plugin;

  @Override
  public String getStatus() {
    return "Talking to NPC";
  }

  @Override
  public boolean validate() {
    return plugin.getCurrentHome() != null
        && plugin.getCurrentHome().isInHome(Players.getLocal())
        && plugin.hasFixed()
        && Hotspot.isEverythingFixed();
  }

  @Override
  public void execute() {
    NPC npc = NPCs.getNearest(plugin.getCurrentHome().getNpcId());

    if (npc == null) {
      plugin.useStairs();

      npc = NPCs.getNearest(plugin.getCurrentHome().getNpcId());

      if (npc == null) {
        return;
      }
    }

    final int maxTries = 5;
    int tries = 0;

    while (!Reachable.isInteractable(npc) && tries++ < maxTries) {
      if (!ChaosMovement.openDoor(npc)) {
        return;
      }
    }

    final NPC finalNpc = npc;
    GameThread.invoke(() -> finalNpc.interact("Talk-to"));

    Time.sleepTicksUntil(Dialog::canContinue, 20);

    Dialog.invokeDialog(
        DialogOption.PLAYER_CONTINUE,
        DialogOption.NPC_CONTINUE,
        DialogOption.CHAT_OPTION_ONE,
        DialogOption.PLAYER_CONTINUE
    );

    Time.sleepTicks(2);

    plugin.setPreviousHome(plugin.getCurrentHome());
    plugin.setCurrentHome(null);
  }
}
