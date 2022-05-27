package io.reisub.unethicalite.secondarygatherer.tasks;

import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.GameState;
import net.runelite.api.Player;
import net.runelite.api.Varbits;
import net.runelite.api.events.PlayerSpawned;
import net.runelite.client.eventbus.Subscribe;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.game.Game;
import net.unethicalite.api.game.Vars;
import net.unethicalite.api.game.Worlds;
import net.unethicalite.client.Static;

public class Hop extends Task {

  private boolean shouldHop;

  @Override
  public String getStatus() {
    return "Hopping worlds";
  }

  @Override
  public boolean validate() {
    return shouldHop;
  }

  @Override
  public void execute() {
    Worlds.hopTo(Worlds.getRandom(w -> {
      if (w.isSkillTotal()) {
        try {
          int totalRequirement =
              Integer.parseInt(w.getActivity().substring(0, w.getActivity().indexOf(" ")));

          return Static.getClient().getTotalLevel() >= totalRequirement;
        } catch (NumberFormatException e) {
          return false;
        }
      }

      return w.isMembers() && !w.isAllPkWorld() && !w.isTournament() && !w.isLeague();
    }));

    if (Time.sleepTicksUntil(() -> Static.getClient().getGameState() != GameState.LOGGED_IN, 3)) {
      shouldHop = false;
    }
  }

  @Subscribe(priority = 100)
  private void onPlayerSpawned(PlayerSpawned event) {
    if (Vars.getBit(Varbits.IN_WILDERNESS) == 0
        || event.getPlayer() == null) {
      return;
    }

    final int wildyLevel = Game.getWildyLevel();
    final int minLevel = Players.getLocal().getCombatLevel() - wildyLevel;
    final int maxLevel = Players.getLocal().getCombatLevel() + wildyLevel;

    final Player enemy = event.getPlayer();

    if (enemy.getSkullIcon() != null
        && enemy.getCombatLevel() <= maxLevel
        && enemy.getCombatLevel() >= minLevel) {
      shouldHop = true;
    }
  }
}
