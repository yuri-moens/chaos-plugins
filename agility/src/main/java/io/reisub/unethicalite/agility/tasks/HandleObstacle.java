package io.reisub.unethicalite.agility.tasks;

import com.google.common.collect.ImmutableSet;
import io.reisub.unethicalite.agility.Agility;
import io.reisub.unethicalite.agility.Config;
import io.reisub.unethicalite.agility.Course;
import io.reisub.unethicalite.utils.tasks.Task;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.AnimationID;
import net.runelite.api.DynamicObject;
import net.runelite.api.GameObject;
import net.runelite.api.Skill;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.StatChanged;
import net.runelite.client.eventbus.Subscribe;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.movement.Movement;

public class HandleObstacle extends Task {
  private static final Set<WorldPoint> NO_EXPERIENCE_POINTS =
      ImmutableSet.of(new WorldPoint(2269, 3393, 2), new WorldPoint(2243, 3394, 2));
  @Inject private Config config;
  private boolean ready;
  private int timeout;
  private int idleTicks;

  @Override
  public String getStatus() {
    return "Handling obstacle";
  }

  @Override
  public boolean validate() {
    if ((config.highAlch() || config.fletch())
        && Movement.getDestination() != null
        && Players.getLocal().distanceTo(Movement.getDestination()) > 5) {
      return false;
    }

    if (NO_EXPERIENCE_POINTS.contains(Players.getLocal().getWorldLocation())) {
      return true;
    }

    return ready;
  }

  @Override
  public void execute() {
    if (Agility.DELAY_POINTS.contains(Players.getLocal().getWorldLocation())) {
      Time.sleepTick();
    }

    if (config.courseSelection() == Course.PRIFDDINAS) {
      TileObject portal =
          TileObjects.getNearest(
              (o) -> {
                if (o instanceof GameObject
                    && ((GameObject) o).getRenderable() instanceof DynamicObject) {
                  return ((DynamicObject) ((GameObject) o).getRenderable()).getAnimationID()
                      == 8456;
                }

                return false;
              });

      if (portal != null
          && portal.getClickbox() != null
          && config.courseSelection().isReachable(portal)) {
        portal.interact(0);
        ready = false;
        return;
      }
    }

    int id = config.courseSelection().getNextObstacleId();
    if (id == 0) {
      return;
    }

    final TileObject obstacle = TileObjects.getNearest(id);
    if (obstacle == null) {
      return;
    }

    GameThread.invoke(() -> obstacle.interact(0));
    ready = false;
  }

  @Subscribe
  private void onStatChanged(StatChanged event) {
    if (event.getSkill() == Skill.AGILITY || event.getSkill() == Skill.MAGIC) {
      ready = true;
    }
  }

  @Subscribe
  private void onHitsplatApplied(HitsplatApplied event) {
    if (event.getHitsplat() != null && event.getHitsplat().isMine()) {
      timeout = 2;
    }
  }

  @Subscribe
  private void onGameTick(GameTick event) {
    final int id = config.courseSelection().getNextObstacleId();

    final TileObject obstacle = TileObjects.getNearest(id);
    final int animationId = Players.getLocal().getAnimation();

    if (obstacle != null
        && config.fletch()
        && !Players.getLocal().isMoving()
        && (animationId == AnimationID.FLETCHING_ATTACH_FEATHERS_TO_ARROWSHAFT
        || animationId == AnimationID.FLETCHING_ATTACH_HEADS)
        && Players.getLocal().distanceTo(obstacle) <= 1) {
      ready = true;
    }

    if (Players.getLocal().isIdle()) {
      idleTicks++;
      if (idleTicks >= timeout) {
        ready = true;
        timeout = 15;
      }
    } else {
      idleTicks = 0;
    }
  }
}
