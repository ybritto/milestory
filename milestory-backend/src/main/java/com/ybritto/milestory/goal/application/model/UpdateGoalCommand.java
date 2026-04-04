package com.ybritto.milestory.goal.application.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record UpdateGoalCommand(
        String title,
        UUID categoryId,
        BigDecimal targetValue,
        String unit,
        String motivation,
        String notes,
        String suggestionBasis,
        boolean customizedFromSuggestion,
        List<GoalCheckpointInput> checkpoints
) {
}
