package com.ybritto.milestory.goal.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ybritto.milestory.goal.application.model.CreateCustomGoalCategoryCommand;
import com.ybritto.milestory.goal.application.model.GoalCategory;
import com.ybritto.milestory.goal.support.GoalTestSupport;
import org.junit.jupiter.api.Test;

class CreateCustomGoalCategoryUseCaseTest {

    @Test
    void createsReusableCustomCategory() {
        GoalTestSupport.InMemoryGoalCategoryPersistencePort categoryPort = new GoalTestSupport.InMemoryGoalCategoryPersistencePort();
        CreateCustomGoalCategoryUseCase useCase = new CreateCustomGoalCategoryUseCase(categoryPort);

        GoalCategory category = useCase.create(new CreateCustomGoalCategoryCommand("Creative Practice"));

        assertEquals("Creative Practice", category.displayName());
        assertFalse(category.systemDefined());
        assertTrue(category.key().startsWith("custom-"));
        assertEquals(category, categoryPort.findCategoryById(category.categoryId()).orElseThrow());
    }
}
