package com.ybritto.milestory.goal.application.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record GoalCheckpointInput(
        LocalDate checkpointDate,
        BigDecimal targetValue,
        String note,
        String origin
) {
}
