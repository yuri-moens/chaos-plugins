package io.reisub.unethicalite.barrows.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.TileObjects;
import dev.unethicalite.api.game.GameThread;
import dev.unethicalite.client.Static;
import io.reisub.unethicalite.barrows.Room;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.NullObjectID;
import net.runelite.api.TileObject;

public class OpenChest extends Task {
  private TileObject chest;

  @Override
  public String getStatus() {
    return "Opening chest";
  }

  @Override
  public boolean validate() {
    return Room.getCurrentRoom() == Room.C
        && !Room.isInCorridor()
        && (Static.getClient().getHintArrowNpc() == null
            || Static.getClient().getHintArrowNpc().isDead())
        && (chest =
                TileObjects.getNearest(
                    o -> o.getId() == NullObjectID.NULL_20973 && o.hasAction("Open")))
            != null;
  }

  @Override
  public void execute() {
    GameThread.invoke(() -> chest.interact("Open"));

    Time.sleepTicksUntil(
        () ->
            TileObjects.getNearest(
                    o -> o.getId() == NullObjectID.NULL_20973 && o.hasAction("Search"))
                != null,
        20);
  }
}
