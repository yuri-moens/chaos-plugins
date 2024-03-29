package io.reisub.unethicalite.chompychomper.tasks;

import io.reisub.unethicalite.chompychomper.ChompyChomper;
import io.reisub.unethicalite.utils.api.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import java.util.LinkedList;
import javax.inject.Inject;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.client.eventbus.Subscribe;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.game.GameThread;

public class Shoot extends Task {
  private final LinkedList<NPC> chompies = new LinkedList<>();
  @Inject private ChompyChomper plugin;

  @Override
  public String getStatus() {
    return "Shooting chompy";
  }

  @Override
  public boolean validate() {
    return plugin.isCurrentActivity(Activity.IDLE) && !chompies.isEmpty();
  }

  @Override
  public void execute() {
    NPC chompy = chompies.poll();
    if (chompy == null || chompy.isDead()) {
      return;
    }

    GameThread.invoke(() -> chompy.interact("Attack"));
    if (!Time.sleepTicksUntil(() -> plugin.isCurrentActivity(Activity.ATTACKING), 3)) {
      chompies.addFirst(chompy);
    }
  }

  @Subscribe
  private void onNpcSpawned(NpcSpawned event) {
    if (!plugin.isRunning()) {
      return;
    }

    if (event.getNpc().getId() == NpcID.CHOMPY_BIRD) {
      chompies.add(event.getNpc());
    }
  }

  @Subscribe
  private void onNpcDespawned(NpcDespawned event) {
    if (!plugin.isRunning()) {
      return;
    }

    if (event.getNpc().getId() == NpcID.CHOMPY_BIRD) {
      chompies.remove(event.getNpc());
    }
  }
}
