package com.ybritto.milestory.goal.out.adapter;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoalJpaRepository extends JpaRepository<GoalJpaEntity, UUID> {

    @EntityGraph(attributePaths = {"category", "checkpoints"})
    List<GoalJpaEntity> findAllByStatusOrderByUpdatedAtDesc(String status);

    @EntityGraph(attributePaths = {"category", "checkpoints"})
    List<GoalJpaEntity> findAllByOrderByUpdatedAtDesc();

    @EntityGraph(attributePaths = {"category", "checkpoints"})
    java.util.Optional<GoalJpaEntity> findById(UUID goalId);
}
