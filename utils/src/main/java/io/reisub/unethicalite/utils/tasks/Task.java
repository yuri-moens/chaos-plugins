package io.reisub.unethicalite.utils.tasks;

public abstract class Task {
  public abstract String getStatus();

  public abstract boolean validate();

  public abstract void execute();
}
