package com.ybritto.milestory.goal.out.adapter;

import com.ybritto.milestory.goal.application.model.GoalCategory;
import com.ybritto.milestory.goal.application.port.out.GoalCategoryPersistencePort;
import com.ybritto.milestory.goal.application.port.out.GoalPersistencePort;
import com.ybritto.milestory.goal.application.port.out.GoalProgressEntryPersistencePort;
import com.ybritto.milestory.goal.application.usecase.GoalCategoryNotFoundException;
import com.ybritto.milestory.goal.domain.Goal;
import com.ybritto.milestory.goal.domain.GoalProgressEntry;
import com.ybritto.milestory.goal.domain.GoalStatus;
import java.time.Clock;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.ybritto.milestory.goal.out.entity.GoalCategoryJpaEntity;
import com.ybritto.milestory.goal.out.entity.GoalJpaEntity;
import com.ybritto.milestory.goal.out.entity.GoalPersistenceMapper;
import com.ybritto.milestory.goal.out.entity.GoalProgressEntryJpaEntity;
import com.ybritto.milestory.goal.out.repository.GoalCategoryJpaRepository;
import com.ybritto.milestory.goal.out.repository.GoalJpaRepository;
import com.ybritto.milestory.goal.out.repository.GoalProgressEntryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GoalPersistenceAdapter implements GoalPersistencePort, GoalCategoryPersistencePort, GoalProgressEntryPersistencePort {

    private static final String CUSTOM_CATEGORY_PREFIX = "custom-";

    private final GoalJpaRepository goalJpaRepository;
    private final GoalCategoryJpaRepository goalCategoryJpaRepository;
    private final GoalProgressEntryJpaRepository goalProgressEntryJpaRepository;
    private final GoalPersistenceMapper goalPersistenceMapper;
    private final Clock goalClock;

    @Override
    @Transactional
    public Goal save(Goal goal) {
        GoalJpaEntity entity = goalJpaRepository.findById(goal.goalId()).orElseGet(goalPersistenceMapper::newGoalEntity);
        GoalCategoryJpaEntity category = goalCategoryJpaRepository.findById(goal.categoryId())
                .orElseThrow(() -> new GoalCategoryNotFoundException(goal.categoryId()));
        goalPersistenceMapper.copyGoalIntoEntity(goal, category, entity);
        return goalPersistenceMapper.toDomainGoal(goalJpaRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Goal> findById(UUID goalId) {
        return goalJpaRepository.findById(goalId).map(goalPersistenceMapper::toDomainGoal);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Goal> listGoals(GoalStatus status) {
        List<GoalJpaEntity> entities = status == null
                ? goalJpaRepository.findAllByOrderByUpdatedAtDesc()
                : goalJpaRepository.findAllByStatusOrderByUpdatedAtDesc(status.name());
        return entities.stream().map(goalPersistenceMapper::toDomainGoal).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GoalCategory> listCategories() {
        return goalCategoryJpaRepository.findAllByOrderBySystemDefinedDescDisplayNameAsc().stream()
                .map(goalPersistenceMapper::toDomainCategory)
                .sorted(categoryComparator())
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GoalCategory> findCategoryById(UUID categoryId) {
        return goalCategoryJpaRepository.findById(categoryId).map(goalPersistenceMapper::toDomainCategory);
    }

    @Override
    @Transactional
    public GoalCategory createCustomCategory(String displayName) {
        Instant now = Instant.now(goalClock);
        GoalCategoryJpaEntity entity = goalPersistenceMapper.newGoalCategoryEntity();
        entity.setCategoryId(UUID.randomUUID());
        entity.setKey(CUSTOM_CATEGORY_PREFIX + UUID.randomUUID());
        entity.setDisplayName(displayName);
        entity.setSystemDefined(false);
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);
        return goalPersistenceMapper.toDomainCategory(goalCategoryJpaRepository.save(entity));
    }

    @Override
    @Transactional
    public GoalProgressEntry save(GoalProgressEntry entry) {
        GoalProgressEntryJpaEntity entity = goalPersistenceMapper.newGoalProgressEntryEntity();
        goalPersistenceMapper.copyProgressEntryIntoEntity(
                entry,
                goalJpaRepository.getReferenceById(entry.goalId()),
                entity
        );
        return goalPersistenceMapper.toDomainProgressEntry(goalProgressEntryJpaRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<GoalProgressEntry> findByGoalId(UUID goalId) {
        return goalProgressEntryJpaRepository.findAllByGoalGoalIdOrderByEntryDateAscRecordedAtAsc(goalId).stream()
                .map(goalPersistenceMapper::toDomainProgressEntry)
                .toList();
    }

    private Comparator<GoalCategory> categoryComparator() {
        List<String> categoryOrder = List.of("financial", "fitness", "reading", "weight", "custom");
        return Comparator
                .comparingInt((GoalCategory category) -> {
                    int index = categoryOrder.indexOf(normalizedCategoryKey(category.key()));
                    return index >= 0 ? index : categoryOrder.size();
                })
                .thenComparing(GoalCategory::displayName, String.CASE_INSENSITIVE_ORDER);
    }

    private String normalizedCategoryKey(String key) {
        if (key == null) {
            return "custom";
        }
        if (key.startsWith(CUSTOM_CATEGORY_PREFIX)) {
            return "custom";
        }
        return key;
    }
}
