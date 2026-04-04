package com.ybritto.milestory.goal.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ybritto.milestory.goal.application.model.GoalCheckpointInput;
import com.ybritto.milestory.goal.application.model.UpdateGoalCommand;
import com.ybritto.milestory.goal.domain.Goal;
import com.ybritto.milestory.goal.domain.GoalCheckpoint;
import com.ybritto.milestory.goal.domain.GoalStatus;
import com.ybritto.milestory.goal.support.GoalTestSupport;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.Year;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class UpdateGoalUseCaseTest {

    @Test
    void updatesActiveGoalDetailsAndCheckpointPlan() {
        GoalTestSupport.InMemoryGoalCategoryPersistencePort categoryPort = new GoalTestSupport.InMemoryGoalCategoryPersistencePort();
        GoalTestSupport.InMemoryGoalPersistencePort goalPort = new GoalTestSupport.InMemoryGoalPersistencePort();
        Goal existingGoal = GoalTestSupport.activeGoal(
                UUID.fromString("00000000-0000-0000-0000-000000000201"),
                UUID.fromString("00000000-0000-0000-0000-000000000103"),
                "Read 24 books",
                BigDecimal.valueOf(24),
                "books",
                "Build a steadier reading habit",
                "Read every week",
                "CATEGORY_AWARE",
                false,
                GoalTestSupport.checkpoints(BigDecimal.valueOf(24))
        );
        goalPort.add(existingGoal);

        UpdateGoalUseCase useCase = new UpdateGoalUseCase(goalPort, categoryPort, GoalTestSupport.FIXED_CLOCK);
        Goal updated = useCase.update(existingGoal.goalId(), new UpdateGoalCommand(
                "Read 30 books",
                UUID.fromString("00000000-0000-0000-0000-000000000102"),
                BigDecimal.valueOf(30),
                "books",
                "Build a steadier reading habit",
                "Read more essays",
                "CATEGORY_AWARE",
                true,
                monthlyCheckpoints(30)
        ));

        assertEquals("Read 30 books", updated.title());
        assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000102"), updated.categoryId());
        assertEquals(BigDecimal.valueOf(30), updated.targetValue());
        assertEquals("Read more essays", updated.notes());
        assertEquals(12, updated.checkpoints().size());
        assertEquals(BigDecimal.valueOf(30), updated.checkpoints().getLast().targetValue());
        assertEquals(updated, goalPort.findById(existingGoal.goalId()).orElseThrow());
    }

    @Test
    void rejectsUpdatesToArchivedGoals() {
        GoalTestSupport.InMemoryGoalCategoryPersistencePort categoryPort = new GoalTestSupport.InMemoryGoalCategoryPersistencePort();
        GoalTestSupport.InMemoryGoalPersistencePort goalPort = new GoalTestSupport.InMemoryGoalPersistencePort();
        Goal archivedGoal = GoalTestSupport.goal(
                UUID.fromString("00000000-0000-0000-0000-000000000202"),
                Year.of(2026),
                UUID.fromString("00000000-0000-0000-0000-000000000103"),
                "Read 24 books",
                BigDecimal.valueOf(24),
                "books",
                "Build a steadier reading habit",
                "Read every week",
                "CATEGORY_AWARE",
                false,
                GoalStatus.ARCHIVED,
                Instant.parse("2026-03-04T00:00:00Z"),
                Instant.parse("2026-01-01T00:00:00Z"),
                Instant.parse("2026-03-04T00:00:00Z"),
                GoalTestSupport.checkpoints(BigDecimal.valueOf(24))
        );
        goalPort.add(archivedGoal);

        UpdateGoalUseCase useCase = new UpdateGoalUseCase(goalPort, categoryPort, GoalTestSupport.FIXED_CLOCK);

        assertThrows(IllegalStateException.class, () -> useCase.update(archivedGoal.goalId(), new UpdateGoalCommand(
                "Read 30 books",
                archivedGoal.categoryId(),
                BigDecimal.valueOf(30),
                "books",
                archivedGoal.motivation(),
                archivedGoal.notes(),
                archivedGoal.suggestionBasis(),
                true,
                monthlyCheckpoints(30)
        )));
    }

    private List<GoalCheckpointInput> monthlyCheckpoints(int targetValue) {
        return GoalTestSupport.checkpoints(BigDecimal.valueOf(targetValue)).stream()
                .map(checkpoint -> new GoalCheckpointInput(
                        checkpoint.checkpointDate(),
                        checkpoint.targetValue(),
                        checkpoint.note(),
                        checkpoint.origin()
                ))
                .toList();
    }
}
