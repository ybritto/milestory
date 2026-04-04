package com.ybritto.milestory.goal.application.model;

public record RestoreGoalCommand(
        Mode mode
) {

    public enum Mode {
        KEEP_EXISTING,
        REGENERATE
    }
}
