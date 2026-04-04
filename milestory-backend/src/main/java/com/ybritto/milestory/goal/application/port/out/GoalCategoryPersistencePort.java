package com.ybritto.milestory.goal.application.port.out;

import com.ybritto.milestory.goal.application.model.GoalCategory;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GoalCategoryPersistencePort {

    List<GoalCategory> listCategories();

    Optional<GoalCategory> findCategoryById(UUID categoryId);

    GoalCategory createCustomCategory(String displayName);
}
