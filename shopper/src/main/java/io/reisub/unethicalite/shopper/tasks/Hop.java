package io.reisub.unethicalite.shopper.tasks;

import io.reisub.unethicalite.shopper.Config;
import io.reisub.unethicalite.shopper.Shopper;
import io.reisub.unethicalite.utils.tasks.Task;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import javax.inject.Inject;
import net.runelite.api.GameState;
import net.runelite.api.World;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.eventbus.Subscribe;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.game.Game;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.game.Worlds;
import net.unethicalite.api.items.Shop;
import net.unethicalite.api.packets.DialogPackets;
import net.unethicalite.client.Static;

public class Hop extends Task {
  @Inject private Shopper plugin;

  @Inject private Config config;

  private Queue<World> worldQueue;
  private int last;

  @Override
  public String getStatus() {
    return "Hopping to next world";
  }

  @Override
  public boolean validate() {
    return plugin.isHop() && last + 5 <= Static.getClient().getTickCount();
  }

  @Override
  public void execute() {
    if (worldQueue == null) {
      initializeWorldQueue();
    }

    World world = worldQueue.poll();
    if (world == null) {
      initializeWorldQueue();
      return;
    }

    worldQueue.add(world);

    if (Shop.isOpen()) {
      DialogPackets.closeInterface();
      if (!Time.sleepTicksUntil(() -> !Shop.isOpen(), 5)) {
        return;
      }
    }

    Worlds.hopTo(world);
    Time.sleepTicksUntil(() -> Game.getState() == GameState.LOGGED_IN, 20);

    plugin.setHop(false);
    last = Static.getClient().getTickCount();
  }

  @Subscribe
  private void onChatMessage(ChatMessage event) {
    if (event.getMessage().contains("before using the World Switcher")) {
      GameThread.invoke(DialogPackets::closeInterface);
    }
  }

  private void initializeWorldQueue() {
    worldQueue = new LinkedList<>();

    int currentId = Worlds.getCurrentId();

    List<World> worlds =
        Worlds.getAll(
            (w) -> {
              if (config.f2pOnly() && w.isMembers()) {
                return false;
              }

              if (config.p2pOnly() && !w.isMembers()) {
                return false;
              }

              if (w.isAllPkWorld() || w.isTournament() || w.isLeague()) {
                return false;
              }

              if (w.isSkillTotal()) {
                try {
                  int totalRequirement =
                      Integer.parseInt(w.getActivity().substring(0, w.getActivity().indexOf(" ")));

                  return Static.getClient().getTotalLevel() >= totalRequirement;
                } catch (NumberFormatException e) {
                  return false;
                }
              }

              return true;
            });

    worldQueue.addAll(worlds);

    while (true) {
      World world = worldQueue.poll();
      worldQueue.add(world);

      if (world == null || world.getId() == currentId) {
        break;
      }
    }
  }
}
