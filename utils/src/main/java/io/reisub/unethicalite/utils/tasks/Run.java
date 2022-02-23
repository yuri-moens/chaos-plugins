package io.reisub.unethicalite.utils.tasks;

import dev.hoot.api.commons.Rand;
import dev.hoot.api.movement.Movement;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Run extends Task {
    private int threshold;

    @Getter
    @Setter
    private int min;

    @Getter
    @Setter
    private int max;

    public Run() {
        this(70, 95);
    }

    public Run(int min, int max) {
        setMin(min);
        setMax(max);
    }

    @Override
    public String getStatus() {
        return "Enabling run";
    }

    @Override
    public boolean validate() {
        return !Movement.isRunEnabled() && Movement.getRunEnergy() >= threshold;
    }

    @Override
    public void execute() {
        Movement.toggleRun();
        threshold = Rand.nextInt(min, max);
    }

    public void setRange(int min, int max) {
        setMin(min);
        setMax(max);
    }
}
