package com.ybritto.milestory.goal.application.usecase;

import com.ybritto.milestory.goal.application.port.out.GoalPersistencePort;
import com.ybritto.milestory.goal.domain.Goal;
import java.util.Objects;
import java.util.UUID;

public class GetGoalDetailUseCase {

    private final GoalPersistencePort goalPersistencePort;

    public GetGoalDetailUseCase(GoalPersistencePort goalPersistencePort) {
        this.goalPersistencePort = Objects.requireNonNull(goalPersistencePort, "goalPersistencePort must not be null");
    }

    public Goal getGoal(UUID goalId) {
        UUID id = Objects.requireNonNull(goalId, "goalId must not be null");
        return goalPersistencePort.findById(id).orElseThrow(() -> new GoalNotFoundException(id));
    }
}
