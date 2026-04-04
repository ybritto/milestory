package com.ybritto.milestory.goal.application.usecase;

import com.ybritto.milestory.goal.application.model.GoalCategory;
import com.ybritto.milestory.goal.application.port.out.GoalCategoryPersistencePort;
import java.util.List;
import java.util.Objects;

public class ListGoalCategoriesUseCase {

    private final GoalCategoryPersistencePort goalCategoryPersistencePort;

    public ListGoalCategoriesUseCase(GoalCategoryPersistencePort goalCategoryPersistencePort) {
        this.goalCategoryPersistencePort = Objects.requireNonNull(goalCategoryPersistencePort, "goalCategoryPersistencePort must not be null");
    }

    public List<GoalCategory> listGoalCategories() {
        return goalCategoryPersistencePort.listCategories();
    }
}
