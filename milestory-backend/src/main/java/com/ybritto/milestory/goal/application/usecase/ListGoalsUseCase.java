package com.ybritto.milestory.goal.application.usecase;

import com.ybritto.milestory.goal.application.port.out.GoalPersistencePort;
import com.ybritto.milestory.goal.domain.Goal;
import com.ybritto.milestory.goal.domain.GoalStatus;
import java.util.List;
import java.util.Objects;

public class ListGoalsUseCase {

    private final GoalPersistencePort goalPersistencePort;

    public ListGoalsUseCase(GoalPersistencePort goalPersistencePort) {
        this.goalPersistencePort = Objects.requireNonNull(goalPersistencePort, "goalPersistencePort must not be null");
    }

    public List<Goal> listGoals(GoalStatus status) {
        GoalStatus effectiveStatus = status == null ? GoalStatus.ACTIVE : status;
        return goalPersistencePort.listGoals(effectiveStatus);
    }
}
