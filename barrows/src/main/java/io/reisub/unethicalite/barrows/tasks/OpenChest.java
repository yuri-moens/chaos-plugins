package io.reisub.unethicalite.barrows.tasks;

import io.reisub.unethicalite.barrows.Room;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.NullObjectID;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.client.Static;

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
  public int execute() {
    GameThread.invoke(() -> chest.interact("Open"));

    Time.sleepTicksUntil(
        () ->
            TileObjects.getNearest(
                    o -> o.getId() == NullObjectID.NULL_20973 && o.hasAction("Search"))
                != null,
        20);

    return 1;
  }
}
