package com.ybritto.milestory.goal.application.usecase;

import com.ybritto.milestory.goal.application.model.CreateGoalCommand;
import com.ybritto.milestory.goal.application.model.GoalCheckpointInput;
import com.ybritto.milestory.goal.application.port.out.GoalCategoryPersistencePort;
import com.ybritto.milestory.goal.application.port.out.GoalPersistencePort;
import com.ybritto.milestory.goal.domain.Goal;
import com.ybritto.milestory.goal.domain.GoalCheckpoint;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.Year;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CreateGoalUseCase {

    private final GoalPersistencePort goalPersistencePort;
    private final GoalCategoryPersistencePort goalCategoryPersistencePort;
    private final Clock clock;

    public CreateGoalUseCase(
            GoalPersistencePort goalPersistencePort,
            GoalCategoryPersistencePort goalCategoryPersistencePort,
            Clock clock
    ) {
        this.goalPersistencePort = Objects.requireNonNull(goalPersistencePort, "goalPersistencePort must not be null");
        this.goalCategoryPersistencePort = Objects.requireNonNull(goalCategoryPersistencePort, "goalCategoryPersistencePort must not be null");
        this.clock = Objects.requireNonNull(clock, "clock must not be null");
    }

    public Goal create(CreateGoalCommand command) {
        CreateGoalCommand draft = Objects.requireNonNull(command, "command must not be null");
        goalCategoryPersistencePort.findCategoryById(draft.categoryId())
                .orElseThrow(() -> new GoalCategoryNotFoundException(draft.categoryId()));

        Goal goal = Goal.create(
                UUID.randomUUID(),
                Year.now(clock),
                requireText(draft.title(), "title"),
                draft.categoryId(),
                requirePositive(draft.targetValue(), "targetValue"),
                requireText(draft.unit(), "unit"),
                requireText(draft.motivation(), "motivation"),
                requireText(draft.notes(), "notes"),
                requireText(draft.suggestionBasis(), "suggestionBasis"),
                draft.customizedFromSuggestion(),
                Instant.now(clock),
                toDomainCheckpoints(draft.checkpoints())
        );
        return goalPersistencePort.save(goal);
    }

    private List<GoalCheckpoint> toDomainCheckpoints(List<GoalCheckpointInput> inputs) {
        if (inputs == null || inputs.isEmpty()) {
            throw new IllegalArgumentException("checkpoints must not be empty");
        }

        List<GoalCheckpoint> checkpoints = new java.util.ArrayList<>();
        for (int index = 0; index < inputs.size(); index++) {
            GoalCheckpointInput input = inputs.get(index);
            checkpoints.add(GoalCheckpoint.of(
                    UUID.randomUUID(),
                    index + 1,
                    input.checkpointDate(),
                    requirePositive(input.targetValue(), "targetValue"),
                    input.note(),
                    requireText(input.origin(), "origin")
            ));
        }
        return List.copyOf(checkpoints);
    }

    private static BigDecimal requirePositive(BigDecimal value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " must not be null");
        if (value.signum() <= 0) {
            throw new IllegalArgumentException(fieldName + " must be positive");
        }
        return value;
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return value.trim();
    }
}
