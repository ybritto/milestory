package com.ybritto.milestory.goal.application.usecase;

import com.ybritto.milestory.goal.application.model.GoalPlanPreview;
import com.ybritto.milestory.goal.application.port.out.GoalCategoryPersistencePort;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class PreviewGoalPlanUseCase {

    private static final BigDecimal MONTHS_IN_YEAR = BigDecimal.valueOf(12);
    private static final String SUGGESTED_ORIGIN = "SUGGESTED";
    private static final Set<String> CATEGORY_AWARE_KEYS = Set.of("financial", "fitness", "reading", "weight");

    private final GoalCategoryPersistencePort goalCategoryPersistencePort;
    private final Clock clock;

    public PreviewGoalPlanUseCase() {
        this(null, Clock.systemUTC());
    }

    public PreviewGoalPlanUseCase(Clock clock) {
        this(null, clock);
    }

    public PreviewGoalPlanUseCase(GoalCategoryPersistencePort goalCategoryPersistencePort) {
        this(goalCategoryPersistencePort, Clock.systemUTC());
    }

    public PreviewGoalPlanUseCase(GoalCategoryPersistencePort goalCategoryPersistencePort, Clock clock) {
        this.goalCategoryPersistencePort = goalCategoryPersistencePort;
        this.clock = Objects.requireNonNull(clock, "clock must not be null");
    }

    public GoalPlanPreview preview(PreviewGoalPlanCommand command) {
        return preview(command, Year.now(clock));
    }

    public GoalPlanPreview preview(PreviewGoalPlanCommand command, Year planningYear) {
        PreviewGoalPlanCommand draft = Objects.requireNonNull(command, "command must not be null");
        Year effectivePlanningYear = Objects.requireNonNull(planningYear, "planningYear must not be null");
        BigDecimal targetValue = requirePositive(draft.targetValue(), "targetValue");
        ResolvedCategory category = resolveCategory(draft);
        GoalPlanPreview.SuggestionBasis suggestionBasis = determineSuggestionBasis(category.categoryKey());
        BigDecimal monthlyIncrement = targetValue.divide(MONTHS_IN_YEAR, 4, RoundingMode.HALF_UP);

        List<GoalPlanPreview.Checkpoint> checkpoints = new ArrayList<>();
        for (int monthIndex = 1; monthIndex <= 12; monthIndex++) {
            YearMonth yearMonth = effectivePlanningYear.atMonth(monthIndex);
            BigDecimal checkpointTarget = monthIndex == 12
                    ? targetValue
                    : monthlyIncrement.multiply(BigDecimal.valueOf(monthIndex)).setScale(4, RoundingMode.HALF_UP);
            checkpoints.add(new GoalPlanPreview.Checkpoint(
                    UUID.randomUUID(),
                    monthIndex,
                    yearMonth.atEndOfMonth(),
                    checkpointTarget.stripTrailingZeros(),
                    checkpointNote(category.categoryLabel(), suggestionBasis, monthIndex, effectivePlanningYear),
                    SUGGESTED_ORIGIN,
                    null,
                    null
            ));
        }

        return new GoalPlanPreview(
                effectivePlanningYear,
                requireText(draft.title(), "title"),
                category.categoryId(),
                targetValue.stripTrailingZeros(),
                requireText(draft.unit(), "unit"),
                requireText(draft.motivation(), "motivation"),
                requireText(draft.notes(), "notes"),
                suggestionBasis,
                false,
                plannedPathSummary(category.categoryLabel(), suggestionBasis),
                List.copyOf(checkpoints)
        );
    }

    private ResolvedCategory resolveCategory(PreviewGoalPlanCommand draft) {
        UUID categoryId = Objects.requireNonNull(draft.categoryId(), "categoryId must not be null");
        if (goalCategoryPersistencePort == null) {
            return new ResolvedCategory(categoryId, normalizeCategoryLabel(draft.categoryKey()), normalizeCategoryKey(draft.categoryKey()));
        }

        return goalCategoryPersistencePort.findCategoryById(categoryId)
                .map(category -> new ResolvedCategory(
                        category.categoryId(),
                        category.displayName(),
                        category.key()
                ))
                .orElseThrow(() -> new GoalCategoryNotFoundException(categoryId));
    }

    private GoalPlanPreview.SuggestionBasis determineSuggestionBasis(String categoryKey) {
        if (categoryKey == null || categoryKey.isBlank()) {
            return GoalPlanPreview.SuggestionBasis.GENERIC_FALLBACK;
        }
        return CATEGORY_AWARE_KEYS.contains(categoryKey.trim()) ? GoalPlanPreview.SuggestionBasis.CATEGORY_AWARE
                : GoalPlanPreview.SuggestionBasis.GENERIC_FALLBACK;
    }

    private String checkpointNote(String categoryLabel, GoalPlanPreview.SuggestionBasis basis, int monthIndex, Year planningYear) {
        String label = normalizeCategoryLabel(categoryLabel);
        String basisLabel = basis == GoalPlanPreview.SuggestionBasis.CATEGORY_AWARE ? "category-aware" : "generic fallback";
        return "Month %d of %d for your %s goal. %s suggestion; refine as needed."
                .formatted(monthIndex, planningYear.getValue(), label, basisLabel);
    }

    private String plannedPathSummary(String categoryLabel, GoalPlanPreview.SuggestionBasis basis) {
        if (basis == GoalPlanPreview.SuggestionBasis.CATEGORY_AWARE) {
            return "Even monthly milestones tailored to your %s goal; refine as needed.".formatted(normalizeCategoryLabel(categoryLabel));
        }
        return "Even monthly milestones from January through December; refine the generic fallback to match your real pace.";
    }

    private static BigDecimal requirePositive(BigDecimal value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " must not be null");
        if (value.signum() <= 0) {
            throw new IllegalArgumentException(fieldName + " must be positive");
        }
        return value;
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return value.trim();
    }

    private String normalizeCategoryLabel(String categoryLabel) {
        if (categoryLabel == null || categoryLabel.isBlank()) {
            return "goal";
        }
        return categoryLabel.trim();
    }

    private String normalizeCategoryKey(String categoryKey) {
        if (categoryKey == null || categoryKey.isBlank()) {
            return null;
        }
        return categoryKey.trim();
    }

    public record PreviewGoalPlanCommand(
            String title,
            UUID categoryId,
            String categoryKey,
            BigDecimal targetValue,
            String unit,
            String motivation,
            String notes
    ) {
    }

    private record ResolvedCategory(UUID categoryId, String categoryLabel, String categoryKey) {
    }
}
