package io.reisub.unethicalite.lizardmanshamankiller.tasks;

import io.reisub.unethicalite.lizardmanshamankiller.Room;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.movement.Movement;

public class Move extends Task {

  @Override
  public String getStatus() {
    return "Moving";
  }

  @Override
  public boolean validate() {
    return Room.getCurrentRoom() != null
        && isTileDangerous();
  }

  @Override
  public int execute() {
    final WorldPoint destination = getSafeTile();

    Movement.walk(destination);

    Time.sleepTicksUntil(() -> Players.getLocal().getWorldLocation().equals(destination), 10);
  }

  private boolean isTileDangerous() {
    return false;
  }

  private WorldPoint getSafeTile() {
    return null;
  }
}
