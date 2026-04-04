package com.ybritto.milestory.goal.application.usecase;

import com.ybritto.milestory.goal.application.model.GoalCheckpointInput;
import com.ybritto.milestory.goal.application.model.RestoreGoalCommand;
import com.ybritto.milestory.goal.application.port.out.GoalCategoryPersistencePort;
import com.ybritto.milestory.goal.application.port.out.GoalPersistencePort;
import com.ybritto.milestory.goal.domain.Goal;
import com.ybritto.milestory.goal.domain.GoalCheckpoint;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.Year;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class RestoreGoalUseCase {

    private final GoalPersistencePort goalPersistencePort;
    private final GoalCategoryPersistencePort goalCategoryPersistencePort;
    private final PreviewGoalPlanUseCase previewGoalPlanUseCase;
    private final Clock clock;

    public RestoreGoalUseCase(
            GoalPersistencePort goalPersistencePort,
            GoalCategoryPersistencePort goalCategoryPersistencePort,
            PreviewGoalPlanUseCase previewGoalPlanUseCase,
            Clock clock
    ) {
        this.goalPersistencePort = Objects.requireNonNull(goalPersistencePort, "goalPersistencePort must not be null");
        this.goalCategoryPersistencePort = Objects.requireNonNull(goalCategoryPersistencePort, "goalCategoryPersistencePort must not be null");
        this.previewGoalPlanUseCase = Objects.requireNonNull(previewGoalPlanUseCase, "previewGoalPlanUseCase must not be null");
        this.clock = Objects.requireNonNull(clock, "clock must not be null");
    }

    public Goal restore(UUID goalId, RestoreGoalCommand command) {
        UUID id = Objects.requireNonNull(goalId, "goalId must not be null");
        RestoreGoalCommand draft = Objects.requireNonNull(command, "command must not be null");
        Goal goal = goalPersistencePort.findById(id).orElseThrow(() -> new GoalNotFoundException(id));
        goalCategoryPersistencePort.findCategoryById(goal.categoryId())
                .orElseThrow(() -> new GoalCategoryNotFoundException(goal.categoryId()));

        List<GoalCheckpoint> checkpoints = switch (draft.mode()) {
            case KEEP_EXISTING -> goal.checkpoints();
            case REGENERATE -> regenerateCheckpoints(goal);
        };

        String suggestionBasis = switch (draft.mode()) {
            case KEEP_EXISTING -> goal.suggestionBasis();
            case REGENERATE -> previewGoalPlanUseCase
                    .preview(buildPreviewCommand(goal), goal.planningYear())
                    .suggestionBasis()
                    .name();
        };
        boolean customizedFromSuggestion = draft.mode() == RestoreGoalCommand.Mode.REGENERATE ? false : goal.customizedFromSuggestion();

        Goal restored = goal.restore(suggestionBasis, customizedFromSuggestion, Instant.now(clock), checkpoints);
        return goalPersistencePort.save(restored);
    }

    private List<GoalCheckpoint> regenerateCheckpoints(Goal goal) {
        return previewGoalPlanUseCase.preview(buildPreviewCommand(goal), goal.planningYear())
                .checkpoints()
                .stream()
                .map(checkpoint -> GoalCheckpoint.of(
                        checkpoint.checkpointId(),
                        checkpoint.sequenceNumber(),
                        checkpoint.checkpointDate(),
                        checkpoint.targetValue(),
                        checkpoint.note(),
                        checkpoint.origin(),
                        checkpoint.originalCheckpointDate(),
                        checkpoint.originalTargetValue()
                ))
                .toList();
    }

    private PreviewGoalPlanUseCase.PreviewGoalPlanCommand buildPreviewCommand(Goal goal) {
        return new PreviewGoalPlanUseCase.PreviewGoalPlanCommand(
                goal.title(),
                goal.categoryId(),
                null,
                goal.targetValue(),
                goal.unit(),
                goal.motivation(),
                goal.notes()
        );
    }
}
