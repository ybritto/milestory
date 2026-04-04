package com.ybritto.milestory.goal.application.usecase;

import java.util.UUID;

public class GoalCategoryNotFoundException extends RuntimeException {

    public GoalCategoryNotFoundException(UUID categoryId) {
        super("Unknown goal category: " + categoryId);
    }
}
