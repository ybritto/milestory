package com.ybritto.milestory.goal.application.port.out;

import com.ybritto.milestory.goal.domain.Goal;
import com.ybritto.milestory.goal.domain.GoalStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GoalPersistencePort {

    Goal save(Goal goal);

    Optional<Goal> findById(UUID goalId);

    List<Goal> listGoals(GoalStatus status);
}
