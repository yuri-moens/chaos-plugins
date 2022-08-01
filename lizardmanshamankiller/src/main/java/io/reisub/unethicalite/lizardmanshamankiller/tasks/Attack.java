package io.reisub.unethicalite.lizardmanshamankiller.tasks;

import io.reisub.unethicalite.lizardmanshamankiller.Room;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.tasks.Task;
import java.util.List;
import net.runelite.api.NPC;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.game.GameThread;

public class Attack extends Task {

  @Override
  public String getStatus() {
    return "Attacking";
  }

  @Override
  public boolean validate() {
    return Room.getCurrentRoom() !=  null
        && Players.getLocal().getInteracting() == null;
  }

  @Override
  public void execute() {
    final List<NPC> shamans = NPCs.getAll(Predicates.ids(Constants.LIZARDMAN_SHAMAN_IDS));

    if (shamans == null || shamans.isEmpty()) {
      return;
    }

    NPC target = null;

    for (NPC shaman : shamans) {
      if (shaman.getInteracting() != null && shaman.getInteracting().equals(Players.getLocal())) {
        target = shaman;
      }
    }

    if (target == null) {
      target = shamans.get(0);
    }

    final NPC finalTarget = target;

    GameThread.invoke(() -> finalTarget.interact(0));
  }
}
