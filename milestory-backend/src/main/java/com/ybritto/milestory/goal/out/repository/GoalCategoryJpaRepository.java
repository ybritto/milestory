package com.ybritto.milestory.goal.out.adapter;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoalCategoryJpaRepository extends JpaRepository<GoalCategoryJpaEntity, UUID> {

    List<GoalCategoryJpaEntity> findAllByOrderBySystemDefinedDescDisplayNameAsc();
}
