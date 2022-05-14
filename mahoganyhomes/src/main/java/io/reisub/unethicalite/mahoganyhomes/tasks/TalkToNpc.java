package io.reisub.unethicalite.mahoganyhomes.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.NPCs;
import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.entities.TileObjects;
import dev.unethicalite.api.game.GameThread;
import dev.unethicalite.api.movement.Reachable;
import dev.unethicalite.api.widgets.Dialog;
import io.reisub.unethicalite.mahoganyhomes.Hotspot;
import io.reisub.unethicalite.mahoganyhomes.MahoganyHomes;
import io.reisub.unethicalite.utils.api.ChaosMovement;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.DialogOption;
import net.runelite.api.NPC;
import net.runelite.api.TileObject;

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
        && Hotspot.isEverythingFixed();
  }

  @Override
  public void execute() {
    NPC npc = NPCs.getNearest(plugin.getCurrentHome().getNpcId());

    if (npc == null) {
      useStairs();

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

  private void useStairs() {
    final TileObject stairs = TileObjects.getNearest(
        o -> o.hasAction("Climb-up", "Climb-down")
    );

    if (stairs == null) {
      return;
    }

    while (!Reachable.isInteractable(stairs)) {
      ChaosMovement.openDoor(stairs);
    }

    final int plane = Players.getLocal().getWorldLocation().getPlane();

    if (stairs.hasAction("Climb-up")) {
      GameThread.invoke(() -> stairs.interact("Climb-up"));
    } else if (stairs.hasAction("Climb-down")) {
      GameThread.invoke(() -> stairs.interact("Climb-down"));
    }

    Time.sleepTicksUntil(() -> plane != Players.getLocal().getWorldLocation().getPlane(), 20);
  }
}
