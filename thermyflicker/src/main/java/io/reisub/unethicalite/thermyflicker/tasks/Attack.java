package io.reisub.unethicalite.thermyflicker.tasks;

import com.google.common.collect.ImmutableSet;
import io.reisub.unethicalite.thermyflicker.ThermyFlicker;
import io.reisub.unethicalite.utils.tasks.Task;
import java.util.Set;
import net.runelite.api.Actor;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.client.eventbus.Subscribe;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.client.Static;

public class Attack extends Task {

  private static final Set<Integer> ANIMATION_IDS = ImmutableSet.of(
      1658, // whip attack
      1062 // dds spec
  );

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
  public int execute() {
    GameThread.invoke(() -> thermy.interact("Attack"));

    Time.sleepTicksUntil(() -> Static.getClient().getTickCount() - ThermyFlicker.lastAttack <= 1,
        3);

    return 1;
  }

  @Subscribe
  private void onHitsplatApplied(HitsplatApplied event) {
    if (event.getActor().getId() == NpcID.THERMONUCLEAR_SMOKE_DEVIL) {
      ThermyFlicker.lastAttack = Static.getClient().getTickCount();
    }
  }

  @Subscribe
  private void onAnimationChanged(AnimationChanged event) {
    final Actor actor = event.getActor();

    if (actor == null || !actor.equals(Players.getLocal())) {
      return;
    }

    if (ANIMATION_IDS.contains(event.getActor().getAnimation())) {
      ThermyFlicker.lastAttack = Static.getClient().getTickCount();
    }
  }
}
