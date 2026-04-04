package com.ybritto.milestory.goal.application.usecase;

import com.ybritto.milestory.goal.application.port.out.GoalPersistencePort;
import com.ybritto.milestory.goal.domain.Goal;
import java.time.Clock;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class ArchiveGoalUseCase {

    private final GoalPersistencePort goalPersistencePort;
    private final Clock clock;

    public ArchiveGoalUseCase(GoalPersistencePort goalPersistencePort, Clock clock) {
        this.goalPersistencePort = Objects.requireNonNull(goalPersistencePort, "goalPersistencePort must not be null");
        this.clock = Objects.requireNonNull(clock, "clock must not be null");
    }

    public Goal archive(UUID goalId) {
        UUID id = Objects.requireNonNull(goalId, "goalId must not be null");
        Goal goal = goalPersistencePort.findById(id).orElseThrow(() -> new GoalNotFoundException(id));
        Goal archived = goal.archive(Instant.now(clock));
        return goalPersistencePort.save(archived);
    }
}
