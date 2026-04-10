package com.ybritto.milestory.goal.out.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.ybritto.milestory.goal.application.model.GoalCategory;
import com.ybritto.milestory.goal.domain.Goal;
import com.ybritto.milestory.goal.domain.GoalProgressEntry;
import com.ybritto.milestory.goal.domain.GoalProgressEntryType;
import com.ybritto.milestory.goal.support.GoalTestSupport;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class GoalPersistenceMapperTest {

    private final GoalPersistenceMapper mapper = Mappers.getMapper(GoalPersistenceMapper.class);

    @Test
    void copiesGoalAggregateIntoJpaEntityAndRehydratesIt() {
        Goal goal = GoalTestSupport.activeGoal(
                UUID.fromString("00000000-0000-0000-0000-000000000701"),
                GoalTestSupport.readingCategory().categoryId(),
                "Read 24 books",
                BigDecimal.valueOf(24),
                "books",
                "Build a steady reading rhythm",
                "Mix fiction and essays",
                "CATEGORY_AWARE",
                true,
                GoalTestSupport.checkpoints(BigDecimal.valueOf(24))
        );
        GoalCategoryJpaEntity categoryEntity = categoryEntity(GoalTestSupport.readingCategory());
        GoalJpaEntity entity = mapper.newGoalEntity();

        mapper.copyGoalIntoEntity(goal, categoryEntity, entity);

        assertEquals(goal.goalId(), entity.getGoalId());
        assertEquals(goal.title(), entity.getTitle());
        assertEquals(goal.categoryId(), entity.getCategory().getCategoryId());
        assertEquals(goal.checkpoints().size(), entity.getCheckpoints().size());
        assertEquals(goal.checkpoints().getFirst().checkpointId(), entity.getCheckpoints().getFirst().getCheckpointId());

        Goal rehydrated = mapper.toDomainGoal(entity);

        assertEquals(goal.goalId(), rehydrated.goalId());
        assertEquals(goal.title(), rehydrated.title());
        assertEquals(goal.categoryId(), rehydrated.categoryId());
        assertEquals(goal.checkpoints().size(), rehydrated.checkpoints().size());
        assertEquals(goal.checkpoints().getLast().targetValue(), rehydrated.checkpoints().getLast().targetValue());
    }

    @Test
    void copiesProgressEntryAndCategoryBetweenDomainAndJpaShapes() {
        Goal goal = GoalTestSupport.activeGoal(
                UUID.fromString("00000000-0000-0000-0000-000000000702"),
                GoalTestSupport.readingCategory().categoryId(),
                "Read 12 books",
                BigDecimal.valueOf(12),
                "books",
                "Keep reading each month",
                "Track momentum",
                "CATEGORY_AWARE",
                false,
                GoalTestSupport.checkpoints(BigDecimal.valueOf(12))
        );
        GoalJpaEntity goalEntity = mapper.newGoalEntity();
        mapper.copyGoalIntoEntity(goal, categoryEntity(GoalTestSupport.readingCategory()), goalEntity);

        GoalProgressEntry progressEntry = GoalProgressEntry.record(
                UUID.fromString("00000000-0000-0000-0000-000000000799"),
                goal.goalId(),
                LocalDate.of(2026, 4, 4),
                BigDecimal.valueOf(5),
                "Finished another book",
                GoalProgressEntryType.NORMAL,
                Instant.parse("2026-04-04T09:00:00Z")
        );
        GoalProgressEntryJpaEntity progressEntryEntity = mapper.newGoalProgressEntryEntity();

        mapper.copyProgressEntryIntoEntity(progressEntry, goalEntity, progressEntryEntity);

        assertEquals(progressEntry.progressEntryId(), progressEntryEntity.getProgressEntryId());
        assertEquals(goal.goalId(), progressEntryEntity.getGoal().getGoalId());

        GoalProgressEntry rehydratedEntry = mapper.toDomainProgressEntry(progressEntryEntity);

        assertEquals(progressEntry.progressEntryId(), rehydratedEntry.progressEntryId());
        assertEquals(progressEntry.progressValue(), rehydratedEntry.progressValue());

        GoalCategory category = mapper.toDomainCategory(categoryEntity(GoalTestSupport.customCategory("Creative Practice")));

        assertNotNull(category.categoryId());
        assertEquals("Creative Practice", category.displayName());
        assertEquals(false, category.systemDefined());
    }

    private GoalCategoryJpaEntity categoryEntity(GoalCategory category) {
        GoalCategoryJpaEntity entity = mapper.newGoalCategoryEntity();
        entity.setCategoryId(category.categoryId());
        entity.setKey(category.key());
        entity.setDisplayName(category.displayName());
        entity.setSystemDefined(category.systemDefined());
        entity.setCreatedAt(GoalTestSupport.FIXED_CLOCK.instant());
        entity.setUpdatedAt(GoalTestSupport.FIXED_CLOCK.instant());
        return entity;
    }
}
