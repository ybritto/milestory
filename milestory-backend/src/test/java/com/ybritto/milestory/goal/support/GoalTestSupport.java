package com.ybritto.milestory.goal.support;

import com.ybritto.milestory.goal.application.model.GoalCategory;
import com.ybritto.milestory.goal.application.port.out.GoalCategoryPersistencePort;
import com.ybritto.milestory.goal.application.port.out.GoalPersistencePort;
import com.ybritto.milestory.goal.domain.Goal;
import com.ybritto.milestory.goal.domain.GoalCheckpoint;
import com.ybritto.milestory.goal.domain.GoalStatus;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.Year;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public final class GoalTestSupport {

    public static final Clock FIXED_CLOCK = Clock.fixed(Instant.parse("2026-04-04T00:00:00Z"), ZoneOffset.UTC);

    private static final UUID FINANCIAL_ID = UUID.fromString("00000000-0000-0000-0000-000000000101");
    private static final UUID FITNESS_ID = UUID.fromString("00000000-0000-0000-0000-000000000102");
    private static final UUID READING_ID = UUID.fromString("00000000-0000-0000-0000-000000000103");
    private static final UUID WEIGHT_ID = UUID.fromString("00000000-0000-0000-0000-000000000104");
    private static final UUID CUSTOM_ID = UUID.fromString("00000000-0000-0000-0000-000000000105");

    private GoalTestSupport() {
    }

    public static GoalCategory financialCategory() {
        return new GoalCategory(FINANCIAL_ID, "financial", "Financial", true);
    }

    public static GoalCategory fitnessCategory() {
        return new GoalCategory(FITNESS_ID, "fitness", "Fitness", true);
    }

    public static GoalCategory readingCategory() {
        return new GoalCategory(READING_ID, "reading", "Reading", true);
    }

    public static GoalCategory weightCategory() {
        return new GoalCategory(WEIGHT_ID, "weight", "Weight", true);
    }

    public static GoalCategory customTemplateCategory() {
        return new GoalCategory(CUSTOM_ID, "custom", "Custom", true);
    }

    public static GoalCategory customCategory(String displayName) {
        return new GoalCategory(UUID.randomUUID(), "custom-" + UUID.randomUUID(), displayName, false);
    }

    public static GoalCheckpoint checkpoint(
            int sequenceNumber,
            LocalDate checkpointDate,
            BigDecimal targetValue,
            String note,
            String origin
    ) {
        return GoalCheckpoint.of(UUID.randomUUID(), sequenceNumber, checkpointDate, targetValue, note, origin);
    }

    public static Goal goal(
            UUID goalId,
            Year planningYear,
            UUID categoryId,
            String title,
            BigDecimal targetValue,
            String unit,
            String motivation,
            String notes,
            String suggestionBasis,
            boolean customizedFromSuggestion,
            GoalStatus status,
            Instant archivedAt,
            Instant createdAt,
            Instant updatedAt,
            List<GoalCheckpoint> checkpoints
    ) {
        return Goal.rehydrate(
                goalId,
                planningYear,
                title,
                categoryId,
                targetValue,
                unit,
                motivation,
                notes,
                status,
                suggestionBasis,
                customizedFromSuggestion,
                archivedAt,
                createdAt,
                updatedAt,
                checkpoints
        );
    }

    public static Goal activeGoal(
            UUID goalId,
            UUID categoryId,
            String title,
            BigDecimal targetValue,
            String unit,
            String motivation,
            String notes,
            String suggestionBasis,
            boolean customizedFromSuggestion,
            List<GoalCheckpoint> checkpoints
    ) {
        Instant now = FIXED_CLOCK.instant();
        return Goal.create(
                goalId,
                Year.of(2026),
                title,
                categoryId,
                targetValue,
                unit,
                motivation,
                notes,
                suggestionBasis,
                customizedFromSuggestion,
                now,
                checkpoints
        );
    }

    public static List<GoalCheckpoint> checkpoints(BigDecimal targetValue) {
        BigDecimal monthlyIncrement = targetValue.divide(BigDecimal.valueOf(12), 4, RoundingMode.HALF_UP);
        List<GoalCheckpoint> checkpoints = new ArrayList<>();
        for (int monthIndex = 1; monthIndex <= 12; monthIndex++) {
            BigDecimal checkpointTarget = monthIndex == 12
                    ? targetValue
                    : monthlyIncrement.multiply(BigDecimal.valueOf(monthIndex)).setScale(4, RoundingMode.HALF_UP);
            checkpoints.add(checkpoint(
                    monthIndex,
                    Year.of(2026).atMonth(monthIndex).atEndOfMonth(),
                    checkpointTarget,
                    "Month " + monthIndex,
                    "SUGGESTED"
            ));
        }
        return checkpoints.stream()
                .sorted(Comparator.comparingInt(GoalCheckpoint::sequenceNumber))
                .toList();
    }

    public static class InMemoryGoalCategoryPersistencePort implements GoalCategoryPersistencePort {

        private final Map<UUID, GoalCategory> categories = new LinkedHashMap<>();

        public InMemoryGoalCategoryPersistencePort() {
            add(financialCategory());
            add(fitnessCategory());
            add(readingCategory());
            add(weightCategory());
            add(customTemplateCategory());
        }

        @Override
        public List<GoalCategory> listCategories() {
            return categories.values().stream()
                    .sorted(Comparator
                            .comparingInt((GoalCategory category) -> categoryOrderIndex(category.key()))
                            .thenComparing(GoalCategory::displayName, String.CASE_INSENSITIVE_ORDER))
                    .toList();
        }

        @Override
        public Optional<GoalCategory> findCategoryById(UUID categoryId) {
            return Optional.ofNullable(categories.get(categoryId));
        }

        @Override
        public GoalCategory createCustomCategory(String displayName) {
            GoalCategory category = customCategory(displayName);
            add(category);
            return category;
        }

        public void add(GoalCategory category) {
            categories.put(category.categoryId(), category);
        }

        private int categoryOrderIndex(String key) {
            List<String> order = List.of("financial", "fitness", "reading", "weight", "custom");
            String normalizedKey = key != null && key.startsWith("custom-") ? "custom" : key;
            int index = order.indexOf(normalizedKey);
            return index >= 0 ? index : order.size();
        }
    }

    public static class InMemoryGoalPersistencePort implements GoalPersistencePort {

        private final Map<UUID, Goal> goals = new LinkedHashMap<>();

        @Override
        public Goal save(Goal goal) {
            goals.put(goal.goalId(), goal);
            return goal;
        }

        @Override
        public Optional<Goal> findById(UUID goalId) {
            return Optional.ofNullable(goals.get(goalId));
        }

        @Override
        public List<Goal> listGoals(GoalStatus status) {
            return goals.values().stream()
                    .filter(goal -> status == null || goal.status() == status)
                    .sorted(Comparator.comparing(Goal::updatedAt).reversed())
                    .toList();
        }

        public void add(Goal goal) {
            goals.put(goal.goalId(), goal);
        }

        public Map<UUID, Goal> goals() {
            return goals;
        }
    }
}
