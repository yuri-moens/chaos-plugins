package io.reisub.unethicalite.utils.tasks;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class Task {
    public abstract String getStatus();

    public abstract boolean validate();

    public abstract void execute();
}
