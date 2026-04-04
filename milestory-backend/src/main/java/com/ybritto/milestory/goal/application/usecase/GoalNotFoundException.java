package com.ybritto.milestory.goal.application.usecase;

import java.util.UUID;

public class GoalNotFoundException extends RuntimeException {

    public GoalNotFoundException(UUID goalId) {
        super("Unknown goal: " + goalId);
    }
}
