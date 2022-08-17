package io.reisub.unethicalite.alchemicalhydrafighter.tasks;

import io.reisub.unethicalite.alchemicalhydrafighter.AlchemicalHydraFighter;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.unethicalite.api.entities.Players;

public class Attack extends Task {

  @Inject
  private AlchemicalHydraFighter plugin;

  @Override
  public String getStatus() {
    return "Attacking";
  }

  @Override
  public boolean validate() {
    return plugin.getPhase() != null
        && plugin.getHydraPlugin().getHydra().getNpc() != null
        && Players.getLocal().getInteracting() == null;
  }

  @Override
  public void execute() {
    assert plugin.getHydraPlugin().getHydra().getNpc() != null;
    plugin.getHydraPlugin().getHydra().getNpc().interact("Attack");
  }
}
