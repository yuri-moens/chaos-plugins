package io.reisub.unethicalite.thermyflicker.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.NPCs;
import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.game.GameThread;
import dev.unethicalite.client.Static;
import io.reisub.unethicalite.thermyflicker.ThermyFlicker;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.client.eventbus.Subscribe;

public class Attack extends Task {

  private NPC thermy;

  @Override
  public String getStatus() {
    return "Attacking";
  }

  @Override
  public boolean validate() {
    thermy = NPCs.getNearest(NpcID.THERMONUCLEAR_SMOKE_DEVIL);

    return Static.getClient().getTickCount() - ThermyFlicker.lastAttack >= 2
        && thermy != null
        && !thermy.isDead()
        && thermy.getInteracting().equals(Players.getLocal());
  }

  @Override
  public void execute() {
    GameThread.invoke(() -> thermy.interact("Attack"));

    Time.sleepTicksUntil(() -> Static.getClient().getTickCount() - ThermyFlicker.lastAttack <= 1, 3);
  }

  @Subscribe
  private void onHitsplatApplied(HitsplatApplied event) {
    if (event.getActor().getId() == NpcID.THERMONUCLEAR_SMOKE_DEVIL) {
      ThermyFlicker.lastAttack =  Static.getClient().getTickCount();
    }
  }
}
