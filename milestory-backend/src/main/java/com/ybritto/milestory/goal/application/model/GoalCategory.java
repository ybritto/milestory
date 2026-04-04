package com.ybritto.milestory.goal.application.model;

import java.util.UUID;

public record GoalCategory(
        UUID categoryId,
        String key,
        String displayName,
        boolean systemDefined
) {
}
