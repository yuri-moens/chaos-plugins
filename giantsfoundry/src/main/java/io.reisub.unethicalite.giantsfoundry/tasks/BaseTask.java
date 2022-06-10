package io.reisub.unethicalite.giantsfoundry.tasks;

import io.reisub.unethicalite.giantsfoundry.Config;
import io.reisub.unethicalite.giantsfoundry.GiantsFoundry;
import io.reisub.unethicalite.utils.tasks.Task;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BaseTask extends Task {
  private final GiantsFoundry plugin;
  private final Config config;

  @Override
  public String getStatus() {
    return "Base tasking";
  }

  @Override
  public boolean validate() {
    return true;
  }

  @Override
  public void execute() {

  }
}