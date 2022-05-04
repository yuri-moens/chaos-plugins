package io.reisub.unethicalite.pickpocket.tasks;

import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.entities.NPCs;
import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.entities.TileObjects;
import dev.unethicalite.api.game.GameThread;
import dev.unethicalite.api.game.Skills;
import dev.unethicalite.api.items.Inventory;
import dev.unethicalite.api.movement.Movement;
import dev.unethicalite.api.movement.Reachable;
import dev.unethicalite.client.Static;
import io.reisub.unethicalite.pickpocket.Config;
import io.reisub.unethicalite.pickpocket.Target;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.api.ChaosMovement;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.NPC;
import net.runelite.api.ObjectID;
import net.runelite.api.Skill;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.eventbus.Subscribe;

public class Pickpocket extends Task {

  @Inject
  private io.reisub.unethicalite.pickpocket.Pickpocket plugin;

  @Inject
  private Config config;

  private int lastStun;

  @Override
  public String getStatus() {
    return "Pickpocketing";
  }

  @Override
  public boolean validate() {
    return plugin.getCurrentActivity() == Activity.IDLE
        && lastStun + 3 < Static.getClient().getTickCount()
        && !Inventory.isFull()
        && Players.getLocal().getModelHeight() != 1000
        && (!config.healAtBank() || !Inventory.contains(config.food()))
        && (Inventory.contains(config.food())
        || Skills.getBoostedLevel(Skill.HITPOINTS) > config.eatHp());
  }

  @Override
  public void execute() {
    final NPC target = NPCs.getNearest(Predicates.ids(config.target().getIds()));
    if (target == null) {
      if (config.target() == Target.VALLESSIA_VON_PITT) {
        goToVallessia();
      } else {
        ChaosMovement.walkTo(plugin.getNearestLocation().getPickpocketLocation(), 2);
      }

      return;
    }

    if (!Reachable.isInteractable(target)) {
      ChaosMovement.walkTo(target.getWorldLocation());

      if (!Time.sleepTicksUntil(() -> Reachable.isInteractable(target), 20)) {
        return;
      }
    }

    GameThread.invoke(() -> target.interact("Pickpocket"));
  }

  @Subscribe
  private void onChatMessage(ChatMessage event) {
    if (!plugin.isRunning()) {
      return;
    }

    if (event.getMessage().contains("You attempt to pick")) {
      plugin.setActivity(Activity.THIEVING);
    } else if (event.getMessage().contains("Your dodgy necklace")
        || event.getMessage().contains("Your attempt to steal goes unnoticed")) {
      plugin.setActivity(Activity.IDLE);
    } else if (event.getMessage().contains("You've been stunned")) {
      plugin.setActivity(Activity.IDLE);
      lastStun = Static.getClient().getTickCount();
    }
  }

  private void goToVallessia() {
    final TileObject stairs = TileObjects.getNearest(ObjectID.STAIRS_38601);
    if (stairs != null) {
      stairs.interact("Climb-up");
      Time.sleepTicksUntil(() -> Utils.isInRegion(14644), 20);
      Time.sleepTicks(3);
    }

    final WorldPoint doorLocation = new WorldPoint(3662, 3378, 0);

    TileObject door = TileObjects.getFirstAt(doorLocation, ObjectID.DOOR_39406);
    if (door != null) {
      door.interact("Open");
      Time.sleepTicksUntil(
          () -> TileObjects.getFirstAt(doorLocation.dy(-1), ObjectID.DOOR_39408) != null, 20);
    }

    Movement.walk(doorLocation);
    Time.sleepTicksUntil(() -> Players.getLocal().getWorldLocation().equals(doorLocation), 10);
    Time.sleepTick();

    door = TileObjects.getFirstAt(doorLocation.dy(-1), ObjectID.DOOR_39408);
    if (door != null) {
      door.interact("Close");
    }

    Inventory.getAll(i -> i.getName().startsWith("Rogue")).forEach(i -> i.interact("Wear"));
  }
}
