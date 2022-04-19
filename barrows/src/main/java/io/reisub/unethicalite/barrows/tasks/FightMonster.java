package io.reisub.unethicalite.barrows.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.NPCs;
import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.game.GameThread;
import dev.unethicalite.api.movement.Reachable;
import dev.unethicalite.api.widgets.Prayers;
import io.reisub.unethicalite.barrows.Barrows;
import io.reisub.unethicalite.barrows.Config;
import io.reisub.unethicalite.barrows.Potential;
import io.reisub.unethicalite.barrows.Room;
import io.reisub.unethicalite.combathelper.CombatHelper;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.NPC;

public class FightMonster extends Task {
  @Inject
  private Barrows plugin;

  @Inject
  private CombatHelper combatHelper;

  @Inject
  private Config config;

  private NPC target;

  @Override
  public String getStatus() {
    return "Fighting monster";
  }

  @Override
  public boolean validate() {
    if (config.potential() == Potential.WHATEVER) {
      return false;
    }

    if (Players.getLocal().getInteracting() != null) {
      return false;
    }

    if (!Utils.isInRegion(Barrows.CRYPT_REGION) && Players.getLocal().getWorldLocation().getPlane() != 0) {
      return false;
    }

    int potential = plugin.getPotentialWithLastBrother();

    if (Room.getCurrentRoom() == Room.C && potential < config.potential().getMinimum()) {
      target = NPCs.getNearest(n -> n.hasAction("Attack") && Reachable.isInteractable(n));

      return target != null;
    }

    target = NPCs.getNearest(n -> n.getInteracting() != null && n.getInteracting().equals(Players.getLocal()));

    if (target == null || !Reachable.isInteractable(target)) {
      return false;
    }

    if (potential + target.getCombatLevel() > config.potential().getMaximum()) {
      return false;
    }

    return potential < config.potential().getMinimum() || config.potential().isTryMaximum();
  }

  @Override
  public void execute() {
    if (!combatHelper.getPrayerHelper().isFlicking() && Prayers.getPoints() > 0) {
      combatHelper.getPrayerHelper().toggleFlicking();
    }

    GameThread.invoke(() -> target.interact("Attack"));
    Time.sleepTicksUntil(() -> Players.getLocal().getInteracting() != null, 3);
  }
}
