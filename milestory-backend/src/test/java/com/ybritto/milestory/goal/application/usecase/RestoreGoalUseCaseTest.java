package com.ybritto.milestory.goal.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ybritto.milestory.goal.application.model.RestoreGoalCommand;
import com.ybritto.milestory.goal.domain.Goal;
import com.ybritto.milestory.goal.domain.GoalStatus;
import com.ybritto.milestory.goal.support.GoalTestSupport;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.Year;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class RestoreGoalUseCaseTest {

    @Test
    void keepsExistingCheckpointPlanWhenRestoreModeDoesNotRegenerate() {
        GoalTestSupport.InMemoryGoalCategoryPersistencePort categoryPort = new GoalTestSupport.InMemoryGoalCategoryPersistencePort();
        GoalTestSupport.InMemoryGoalPersistencePort goalPort = new GoalTestSupport.InMemoryGoalPersistencePort();
        Goal archivedGoal = GoalTestSupport.goal(
                UUID.fromString("00000000-0000-0000-0000-000000000401"),
                Year.of(2026),
                UUID.fromString("00000000-0000-0000-0000-000000000103"),
                "Read 24 books",
                BigDecimal.valueOf(24),
                "books",
                "Build a steadier reading habit",
                "Read every week",
                "CATEGORY_AWARE",
                true,
                GoalStatus.ARCHIVED,
                Instant.parse("2026-03-04T00:00:00Z"),
                Instant.parse("2026-01-01T00:00:00Z"),
                Instant.parse("2026-03-04T00:00:00Z"),
                GoalTestSupport.checkpoints(BigDecimal.valueOf(24))
        );
        goalPort.add(archivedGoal);

        RestoreGoalUseCase useCase = new RestoreGoalUseCase(
                goalPort,
                categoryPort,
                new PreviewGoalPlanUseCase(categoryPort, GoalTestSupport.FIXED_CLOCK),
                GoalTestSupport.FIXED_CLOCK
        );

        Goal restoredGoal = useCase.restore(archivedGoal.goalId(), new RestoreGoalCommand(RestoreGoalCommand.Mode.KEEP_EXISTING));

        assertEquals(GoalStatus.ACTIVE, restoredGoal.status());
        assertTrue(restoredGoal.customizedFromSuggestion());
        assertEquals(12, restoredGoal.checkpoints().size());
        assertEquals(archivedGoal.checkpoints().getLast().targetValue(), restoredGoal.checkpoints().getLast().targetValue());
    }

    @Test
    void regeneratesMonthlyPlanWhenRequested() {
        GoalTestSupport.InMemoryGoalCategoryPersistencePort categoryPort = new GoalTestSupport.InMemoryGoalCategoryPersistencePort();
        GoalTestSupport.InMemoryGoalPersistencePort goalPort = new GoalTestSupport.InMemoryGoalPersistencePort();
        Goal archivedGoal = GoalTestSupport.goal(
                UUID.fromString("00000000-0000-0000-0000-000000000402"),
                Year.of(2026),
                UUID.fromString("00000000-0000-0000-0000-000000000103"),
                "Read 24 books",
                BigDecimal.valueOf(24),
                "books",
                "Build a steadier reading habit",
                "Read every week",
                "GENERIC_FALLBACK",
                true,
                GoalStatus.ARCHIVED,
                Instant.parse("2026-03-04T00:00:00Z"),
                Instant.parse("2026-01-01T00:00:00Z"),
                Instant.parse("2026-03-04T00:00:00Z"),
                GoalTestSupport.checkpoints(BigDecimal.valueOf(24))
        );
        goalPort.add(archivedGoal);

        RestoreGoalUseCase useCase = new RestoreGoalUseCase(
                goalPort,
                categoryPort,
                new PreviewGoalPlanUseCase(categoryPort, GoalTestSupport.FIXED_CLOCK),
                GoalTestSupport.FIXED_CLOCK
        );

        Goal restoredGoal = useCase.restore(archivedGoal.goalId(), new RestoreGoalCommand(RestoreGoalCommand.Mode.REGENERATE));

        assertEquals(GoalStatus.ACTIVE, restoredGoal.status());
        assertFalse(restoredGoal.customizedFromSuggestion());
        assertEquals(12, restoredGoal.checkpoints().size());
        assertEquals(BigDecimal.valueOf(24), restoredGoal.checkpoints().getLast().targetValue());
        assertEquals("CATEGORY_AWARE", restoredGoal.suggestionBasis());
    }
}
