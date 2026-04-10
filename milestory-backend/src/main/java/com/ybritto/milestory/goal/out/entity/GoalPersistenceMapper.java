package com.ybritto.milestory.goal.out.entity;

import com.ybritto.milestory.goal.application.model.GoalCategory;
import com.ybritto.milestory.goal.domain.Goal;
import com.ybritto.milestory.goal.domain.GoalCheckpoint;
import com.ybritto.milestory.goal.domain.GoalProgressEntry;
import com.ybritto.milestory.goal.domain.GoalStatus;
import java.time.Year;
import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class GoalPersistenceMapper {

    public GoalJpaEntity newGoalEntity() {
        return new GoalJpaEntity();
    }

    public GoalCategoryJpaEntity newGoalCategoryEntity() {
        return new GoalCategoryJpaEntity();
    }

    public GoalProgressEntryJpaEntity newGoalProgressEntryEntity() {
        return new GoalProgressEntryJpaEntity();
    }

    @Mapping(target = "goalId", expression = "java(goal.goalId())")
    @Mapping(target = "planningYear", expression = "java(map(goal.planningYear()))")
    @Mapping(target = "title", expression = "java(goal.title())")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "targetValue", expression = "java(goal.targetValue())")
    @Mapping(target = "unit", expression = "java(goal.unit())")
    @Mapping(target = "motivation", expression = "java(goal.motivation())")
    @Mapping(target = "notes", expression = "java(goal.notes())")
    @Mapping(target = "status", expression = "java(map(goal.status()))")
    @Mapping(target = "suggestionBasis", expression = "java(goal.suggestionBasis())")
    @Mapping(target = "customizedFromSuggestion", expression = "java(goal.customizedFromSuggestion())")
    @Mapping(target = "archivedAt", expression = "java(goal.archivedAt())")
    @Mapping(target = "createdAt", expression = "java(goal.createdAt())")
    @Mapping(target = "updatedAt", expression = "java(goal.updatedAt())")
    @Mapping(target = "checkpoints", ignore = true)
    @Mapping(target = "progressEntries", ignore = true)
    public abstract void copyGoalIntoEntity(Goal goal, GoalCategoryJpaEntity category, @MappingTarget GoalJpaEntity entity);

    @AfterMapping
    protected void attachCheckpointEntities(Goal goal, @MappingTarget GoalJpaEntity entity) {
        entity.getCheckpoints().clear();
        List<GoalCheckpointJpaEntity> checkpointEntities = toCheckpointEntities(goal.checkpoints());
        for (GoalCheckpointJpaEntity checkpointEntity : checkpointEntities) {
            checkpointEntity.setGoal(entity);
        }
        entity.getCheckpoints().addAll(checkpointEntities);
    }

    @Mapping(target = "checkpointId", expression = "java(checkpoint.checkpointId())")
    @Mapping(target = "sequenceNumber", expression = "java(checkpoint.sequenceNumber())")
    @Mapping(target = "checkpointDate", expression = "java(checkpoint.checkpointDate())")
    @Mapping(target = "targetValue", expression = "java(checkpoint.targetValue())")
    @Mapping(target = "note", expression = "java(checkpoint.note())")
    @Mapping(target = "origin", expression = "java(checkpoint.origin())")
    @Mapping(target = "originalCheckpointDate", expression = "java(checkpoint.originalCheckpointDate())")
    @Mapping(target = "originalTargetValue", expression = "java(checkpoint.originalTargetValue())")
    @Mapping(target = "goal", ignore = true)
    protected abstract GoalCheckpointJpaEntity toCheckpointEntity(GoalCheckpoint checkpoint);

    protected abstract List<GoalCheckpointJpaEntity> toCheckpointEntities(List<GoalCheckpoint> checkpoints);

    public Goal toDomainGoal(GoalJpaEntity entity) {
        return Goal.rehydrate(
                entity.getGoalId(),
                Year.of(entity.getPlanningYear()),
                entity.getTitle(),
                entity.getCategory().getCategoryId(),
                entity.getTargetValue(),
                entity.getUnit(),
                entity.getMotivation(),
                entity.getNotes(),
                GoalStatus.valueOf(entity.getStatus()),
                entity.getSuggestionBasis(),
                entity.isCustomizedFromSuggestion(),
                entity.getArchivedAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                toDomainCheckpoints(entity.getCheckpoints())
        );
    }

    public GoalCategory toDomainCategory(GoalCategoryJpaEntity entity) {
        return new GoalCategory(
                entity.getCategoryId(),
                entity.getKey(),
                entity.getDisplayName(),
                entity.isSystemDefined()
        );
    }

    @Mapping(target = "progressEntryId", expression = "java(entry.progressEntryId())")
    @Mapping(target = "goal", source = "goal")
    @Mapping(target = "entryDate", expression = "java(entry.entryDate())")
    @Mapping(target = "progressValue", expression = "java(entry.progressValue())")
    @Mapping(target = "note", expression = "java(entry.note())")
    @Mapping(target = "entryType", expression = "java(entry.entryType())")
    @Mapping(target = "recordedAt", expression = "java(entry.recordedAt())")
    public abstract void copyProgressEntryIntoEntity(
            GoalProgressEntry entry,
            GoalJpaEntity goal,
            @MappingTarget GoalProgressEntryJpaEntity entity
    );

    public GoalProgressEntry toDomainProgressEntry(GoalProgressEntryJpaEntity entity) {
        return GoalProgressEntry.record(
                entity.getProgressEntryId(),
                entity.getGoal().getGoalId(),
                entity.getEntryDate(),
                entity.getProgressValue(),
                entity.getNote(),
                entity.getEntryType(),
                entity.getRecordedAt()
        );
    }

    protected List<GoalCheckpoint> toDomainCheckpoints(List<GoalCheckpointJpaEntity> entities) {
        return entities.stream().map(this::toDomainCheckpoint).toList();
    }

    protected GoalCheckpoint toDomainCheckpoint(GoalCheckpointJpaEntity entity) {
        return GoalCheckpoint.of(
                entity.getCheckpointId(),
                entity.getSequenceNumber(),
                entity.getCheckpointDate(),
                entity.getTargetValue(),
                entity.getNote(),
                entity.getOrigin(),
                entity.getOriginalCheckpointDate(),
                entity.getOriginalTargetValue()
        );
    }

    protected Integer map(Year year) {
        return year == null ? null : year.getValue();
    }

    protected String map(GoalStatus status) {
        return status == null ? null : status.name();
    }
}
