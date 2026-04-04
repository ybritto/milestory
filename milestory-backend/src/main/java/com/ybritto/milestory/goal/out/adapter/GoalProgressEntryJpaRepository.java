package com.ybritto.milestory.goal.out.adapter;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoalProgressEntryJpaRepository extends JpaRepository<GoalProgressEntryJpaEntity, UUID> {

    List<GoalProgressEntryJpaEntity> findAllByGoalGoalIdOrderByEntryDateAscRecordedAtAsc(UUID goalId);
}
