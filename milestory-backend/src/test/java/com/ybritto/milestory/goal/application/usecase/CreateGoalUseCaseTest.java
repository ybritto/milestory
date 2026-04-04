package com.ybritto.milestory.goal.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.ybritto.milestory.goal.application.model.CreateGoalCommand;
import com.ybritto.milestory.goal.application.model.GoalCheckpointInput;
import com.ybritto.milestory.goal.domain.Goal;
import com.ybritto.milestory.goal.domain.GoalStatus;
import com.ybritto.milestory.goal.support.GoalTestSupport;
import java.math.BigDecimal;
import java.time.Year;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class CreateGoalUseCaseTest {

    @Test
    void createsGoalForCurrentPlanningYearWithOrderedCheckpoints() {
        GoalTestSupport.InMemoryGoalCategoryPersistencePort categoryPort = new GoalTestSupport.InMemoryGoalCategoryPersistencePort();
        GoalTestSupport.InMemoryGoalPersistencePort goalPort = new GoalTestSupport.InMemoryGoalPersistencePort();
        CreateGoalUseCase useCase = new CreateGoalUseCase(goalPort, categoryPort, GoalTestSupport.FIXED_CLOCK);

        Goal goal = useCase.create(new CreateGoalCommand(
                "Read 24 books",
                UUID.fromString("00000000-0000-0000-0000-000000000103"),
                BigDecimal.valueOf(24),
                "books",
                "Build a steadier reading habit",
                "Read every week",
                "CATEGORY_AWARE",
                false,
                monthlyCheckpoints(24)
        ));

        assertNotNull(goal.goalId());
        assertEquals(Year.of(2026), goal.planningYear());
        assertEquals(GoalStatus.ACTIVE, goal.status());
        assertEquals("Read 24 books", goal.title());
        assertEquals(BigDecimal.valueOf(24), goal.targetValue());
        assertEquals(12, goal.checkpoints().size());
        assertEquals(1, goal.checkpoints().getFirst().sequenceNumber());
        assertEquals(12, goal.checkpoints().getLast().sequenceNumber());
        assertEquals(BigDecimal.valueOf(24), goal.checkpoints().getLast().targetValue());
        assertEquals(goal, goalPort.findById(goal.goalId()).orElseThrow());
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
