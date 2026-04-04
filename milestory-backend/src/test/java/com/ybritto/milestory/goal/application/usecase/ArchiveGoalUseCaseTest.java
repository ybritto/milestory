package com.ybritto.milestory.goal.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.ybritto.milestory.goal.domain.Goal;
import com.ybritto.milestory.goal.domain.GoalStatus;
import com.ybritto.milestory.goal.support.GoalTestSupport;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.Year;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ArchiveGoalUseCaseTest {

    @Test
    void archivesActiveGoalAndPersistsArchivedAt() {
        GoalTestSupport.InMemoryGoalPersistencePort goalPort = new GoalTestSupport.InMemoryGoalPersistencePort();
        Goal activeGoal = GoalTestSupport.goal(
                UUID.fromString("00000000-0000-0000-0000-000000000301"),
                Year.of(2026),
                UUID.fromString("00000000-0000-0000-0000-000000000103"),
                "Read 24 books",
                BigDecimal.valueOf(24),
                "books",
                "Build a steadier reading habit",
                "Read every week",
                "CATEGORY_AWARE",
                false,
                GoalStatus.ACTIVE,
                null,
                Instant.parse("2026-01-01T00:00:00Z"),
                Instant.parse("2026-01-01T00:00:00Z"),
                GoalTestSupport.checkpoints(BigDecimal.valueOf(24))
        );
        goalPort.add(activeGoal);

        ArchiveGoalUseCase useCase = new ArchiveGoalUseCase(goalPort, GoalTestSupport.FIXED_CLOCK);
        Goal archivedGoal = useCase.archive(activeGoal.goalId());

        assertEquals(GoalStatus.ARCHIVED, archivedGoal.status());
        assertNotNull(archivedGoal.archivedAt());
        assertEquals(archivedGoal, goalPort.findById(activeGoal.goalId()).orElseThrow());
    }
}
