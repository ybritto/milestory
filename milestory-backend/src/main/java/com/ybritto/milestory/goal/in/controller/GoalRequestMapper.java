package com.ybritto.milestory.goal.in.controller;

import com.ybritto.milestory.goal.application.model.CreateCustomGoalCategoryCommand;
import com.ybritto.milestory.goal.application.model.CreateGoalCommand;
import com.ybritto.milestory.goal.application.model.GoalCheckpointInput;
import com.ybritto.milestory.goal.application.model.RecordProgressEntryCommand;
import com.ybritto.milestory.goal.application.model.RestoreGoalCommand;
import com.ybritto.milestory.goal.application.model.UpdateGoalCommand;
import com.ybritto.milestory.goal.application.usecase.PreviewGoalPlanUseCase;
import com.ybritto.milestory.goal.domain.GoalStatus;
import com.ybritto.milestory.generated.model.CreateGoalCategoryRequest;
import com.ybritto.milestory.generated.model.CreateGoalRequest;
import com.ybritto.milestory.generated.model.GoalCheckpointRequest;
import com.ybritto.milestory.generated.model.GoalDraftRequest;
import com.ybritto.milestory.generated.model.RecordGoalProgressEntryRequest;
import com.ybritto.milestory.generated.model.RestoreGoalRequest;
import com.ybritto.milestory.generated.model.RestoreGoalRequestMode;
import com.ybritto.milestory.generated.model.Status;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GoalRequestMapper {

    CreateCustomGoalCategoryCommand toCreateCustomGoalCategoryCommand(CreateGoalCategoryRequest request);

    CreateGoalCommand toCreateGoalCommand(CreateGoalRequest request);

    UpdateGoalCommand toUpdateGoalCommand(CreateGoalRequest request);

    PreviewGoalPlanUseCase.PreviewGoalPlanCommand toPreviewGoalPlanCommand(GoalDraftRequest request);

    RestoreGoalCommand toRestoreGoalCommand(RestoreGoalRequest request);

    default RecordProgressEntryCommand toRecordProgressEntryCommand(RecordGoalProgressEntryRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("record progress request must not be null");
        }
        return new RecordProgressEntryCommand(
                request.entryDate(),
                toBigDecimal(request.progressValue()),
                request.note()
        );
    }

    default RestoreGoalCommand.Mode toRestoreMode(RestoreGoalRequestMode mode) {
        if (mode == null) {
            throw new IllegalArgumentException("restore mode must not be null");
        }
        return RestoreGoalCommand.Mode.valueOf(mode.name());
    }

    default GoalStatus toGoalStatus(Status status) {
        return status == null ? null : GoalStatus.valueOf(status.name());
    }

    default List<GoalCheckpointInput> toCheckpointInputs(GoalCheckpointRequest[] checkpoints) {
        if (checkpoints == null) {
            return List.of();
        }
        return Arrays.stream(checkpoints).map(this::toCheckpointInput).toList();
    }

    default GoalCheckpointInput toCheckpointInput(GoalCheckpointRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("checkpoint request must not be null");
        }
        return new GoalCheckpointInput(
                request.checkpointDate(),
                toBigDecimal(request.targetValue()),
                request.note(),
                request.origin() == null ? null : request.origin().name()
        );
    }

    default BigDecimal toBigDecimal(Double value) {
        return value == null ? null : BigDecimal.valueOf(value);
    }

    default UUID toUuid(String value) {
        return value == null ? null : UUID.fromString(value);
    }
}
