package com.ybritto.milestory.goal.application.usecase;

import com.ybritto.milestory.goal.application.model.CreateCustomGoalCategoryCommand;
import com.ybritto.milestory.goal.application.model.GoalCategory;
import com.ybritto.milestory.goal.application.port.out.GoalCategoryPersistencePort;
import java.util.Objects;

public class CreateCustomGoalCategoryUseCase {

    private final GoalCategoryPersistencePort goalCategoryPersistencePort;

    public CreateCustomGoalCategoryUseCase(GoalCategoryPersistencePort goalCategoryPersistencePort) {
        this.goalCategoryPersistencePort = Objects.requireNonNull(goalCategoryPersistencePort, "goalCategoryPersistencePort must not be null");
    }

    public GoalCategory create(CreateCustomGoalCategoryCommand command) {
        CreateCustomGoalCategoryCommand draft = Objects.requireNonNull(command, "command must not be null");
        String displayName = requireText(draft.displayName(), "displayName");
        return goalCategoryPersistencePort.createCustomCategory(displayName);
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return value.trim();
    }
}
