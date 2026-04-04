package com.ybritto.milestory.goal.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ybritto.milestory.goal.application.model.GoalCategory;
import com.ybritto.milestory.goal.support.GoalTestSupport;
import java.util.List;
import org.junit.jupiter.api.Test;

class ListGoalCategoriesUseCaseTest {

    @Test
    void returnsSeededCategoriesAndCustomAdditions() {
        GoalTestSupport.InMemoryGoalCategoryPersistencePort categoryPort = new GoalTestSupport.InMemoryGoalCategoryPersistencePort();
        ListGoalCategoriesUseCase useCase = new ListGoalCategoriesUseCase(categoryPort);

        List<GoalCategory> categories = useCase.listGoalCategories();

        assertEquals(5, categories.size());
        assertTrue(categories.stream().anyMatch(category -> "financial".equals(category.key())));
        assertTrue(categories.stream().anyMatch(category -> "reading".equals(category.key())));

        categoryPort.createCustomCategory("Creative Practice");
        assertEquals(6, useCase.listGoalCategories().size());
        assertTrue(useCase.listGoalCategories().stream().anyMatch(category -> "Creative Practice".equals(category.displayName())));
    }
}
