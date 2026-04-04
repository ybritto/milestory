package com.ybritto.milestory.goal.in.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ybritto.milestory.goal.application.usecase.ArchiveGoalUseCase;
import com.ybritto.milestory.goal.application.usecase.CreateCustomGoalCategoryUseCase;
import com.ybritto.milestory.goal.application.usecase.CreateGoalUseCase;
import com.ybritto.milestory.goal.application.usecase.GetGoalDetailUseCase;
import com.ybritto.milestory.goal.application.usecase.ListGoalCategoriesUseCase;
import com.ybritto.milestory.goal.application.usecase.ListGoalsUseCase;
import com.ybritto.milestory.goal.application.usecase.PreviewGoalPlanUseCase;
import com.ybritto.milestory.goal.application.usecase.RestoreGoalUseCase;
import com.ybritto.milestory.goal.application.usecase.UpdateGoalUseCase;
import com.ybritto.milestory.goal.domain.GoalCheckpoint;
import com.ybritto.milestory.goal.support.GoalTestSupport;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class GoalControllerIntegrationTest {

    private static final UUID READING_CATEGORY_ID = UUID.fromString("00000000-0000-0000-0000-000000000103");
    private static final String READING_CATEGORY_ID_JSON = "\"" + READING_CATEGORY_ID + "\"";
    private GoalTestSupport.InMemoryGoalCategoryPersistencePort categoryPort;
    private GoalTestSupport.InMemoryGoalPersistencePort goalPort;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.categoryPort = new GoalTestSupport.InMemoryGoalCategoryPersistencePort();
        this.goalPort = new GoalTestSupport.InMemoryGoalPersistencePort();

        PreviewGoalPlanUseCase previewGoalPlanUseCase = new PreviewGoalPlanUseCase(categoryPort, GoalTestSupport.FIXED_CLOCK);
        ListGoalCategoriesUseCase listGoalCategoriesUseCase = new ListGoalCategoriesUseCase(categoryPort);
        CreateCustomGoalCategoryUseCase createCustomGoalCategoryUseCase = new CreateCustomGoalCategoryUseCase(categoryPort);
        CreateGoalUseCase createGoalUseCase = new CreateGoalUseCase(goalPort, categoryPort, GoalTestSupport.FIXED_CLOCK);
        GetGoalDetailUseCase getGoalDetailUseCase = new GetGoalDetailUseCase(goalPort);
        ListGoalsUseCase listGoalsUseCase = new ListGoalsUseCase(goalPort);
        UpdateGoalUseCase updateGoalUseCase = new UpdateGoalUseCase(goalPort, categoryPort, GoalTestSupport.FIXED_CLOCK);
        ArchiveGoalUseCase archiveGoalUseCase = new ArchiveGoalUseCase(goalPort, GoalTestSupport.FIXED_CLOCK);
        RestoreGoalUseCase restoreGoalUseCase = new RestoreGoalUseCase(
                goalPort,
                categoryPort,
                previewGoalPlanUseCase,
                GoalTestSupport.FIXED_CLOCK
        );

        GoalController controller = new GoalController(
                previewGoalPlanUseCase,
                listGoalCategoriesUseCase,
                createCustomGoalCategoryUseCase,
                createGoalUseCase,
                getGoalDetailUseCase,
                listGoalsUseCase,
                updateGoalUseCase,
                archiveGoalUseCase,
                restoreGoalUseCase,
                Mappers.getMapper(GoalRequestMapper.class),
                Mappers.getMapper(GoalResponseMapper.class)
        );

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GoalControllerExceptionHandler())
                .build();
    }

    @Test
    void createsCategoriesPreviewsAndPersistsGoals() throws Exception {
        mockMvc.perform(get("/api/v1/goal-categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].key", is("financial")));

        mockMvc.perform(post("/api/v1/goal-categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"displayName\":\"Creative Practice\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.displayName", is("Creative Practice")))
                .andExpect(jsonPath("$.systemDefined", is(false)));

        mockMvc.perform(get("/api/v1/goal-categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(6)))
                .andExpect(jsonPath("$[4].displayName", is("Creative Practice")));

        previewGoalPlan();
        mockMvc.perform(post("/api/v1/goals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createGoalRequestJson("Read 24 books", BigDecimal.valueOf(24))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("Read 24 books")))
                .andExpect(jsonPath("$.status", is("ACTIVE")))
                .andExpect(jsonPath("$.checkpoints", hasSize(12)))
                .andExpect(jsonPath("$.plannedPathSummary", notNullValue()));

        UUID goalId = goalPort.goals().keySet().iterator().next();

        mockMvc.perform(get("/api/v1/goals/{goalId}", goalId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.goalId", is(goalId.toString())))
                .andExpect(jsonPath("$.status", is("ACTIVE")))
                .andExpect(jsonPath("$.checkpoints", hasSize(12)))
                .andExpect(jsonPath("$.plannedPathSummary", notNullValue()));

        mockMvc.perform(get("/api/v1/goals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].goalId", is(goalId.toString())));

        mockMvc.perform(get("/api/v1/goals").param("status", "ARCHIVED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void updatesArchivesRestoresAndBlocksArchivedEdits() throws Exception {
        previewGoalPlan();
        mockMvc.perform(post("/api/v1/goals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createGoalRequestJson("Read 24 books", BigDecimal.valueOf(24))))
                .andExpect(status().isCreated());

        UUID goalId = goalPort.goals().keySet().iterator().next();

        mockMvc.perform(put("/api/v1/goals/{goalId}", goalId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createGoalRequestJson("Read 30 books", BigDecimal.valueOf(30))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Read 30 books")))
                .andExpect(jsonPath("$.targetValue", is(30.0)));

        mockMvc.perform(post("/api/v1/goals/{goalId}/archive", goalId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("ARCHIVED")))
                .andExpect(jsonPath("$.archivedAt", notNullValue()));

        mockMvc.perform(get("/api/v1/goals").param("status", "ACTIVE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        mockMvc.perform(get("/api/v1/goals").param("status", "ARCHIVED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].goalId", is(goalId.toString())));

        mockMvc.perform(put("/api/v1/goals/{goalId}", goalId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createGoalRequestJson("Read 32 books", BigDecimal.valueOf(32))))
                .andExpect(status().isConflict());

        mockMvc.perform(post("/api/v1/goals/{goalId}/restore", goalId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"mode\":\"KEEP_EXISTING\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("ACTIVE")))
                .andExpect(jsonPath("$.checkpoints", hasSize(12)));

        mockMvc.perform(get("/api/v1/goals/{goalId}", goalId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("ACTIVE")));
    }

    private void previewGoalPlan() throws Exception {
        mockMvc.perform(post("/api/v1/goal-plans/previews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Read 24 books",
                                  "categoryId": %s,
                                  "targetValue": 24,
                                  "unit": "books",
                                  "motivation": "Build a steadier reading habit",
                                  "notes": "Read every week"
                                }
                                """.formatted(READING_CATEGORY_ID_JSON)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.planningYear", is(2026)))
                .andExpect(jsonPath("$.suggestionBasis", is("CATEGORY_AWARE")))
                .andExpect(jsonPath("$.checkpoints", hasSize(12)));
    }

    private String createGoalRequestJson(String title, BigDecimal targetValue) {
        String checkpointsJson = GoalTestSupport.checkpoints(targetValue).stream()
                .map(this::toCheckpointJson)
                .reduce((left, right) -> left + "," + right)
                .orElse("");

        return """
                {
                  "title": "%s",
                  "categoryId": %s,
                  "targetValue": %s,
                  "unit": "books",
                  "motivation": "Build a steadier reading habit",
                  "notes": "Read every week",
                  "suggestionBasis": "CATEGORY_AWARE",
                  "customizedFromSuggestion": false,
                  "checkpoints": [%s]
                }
                """.formatted(title, READING_CATEGORY_ID_JSON, targetValue.toPlainString(), checkpointsJson);
    }

    private String toCheckpointJson(GoalCheckpoint checkpoint) {
        return """
                {
                  "checkpointDate": "%s",
                  "targetValue": %s,
                  "note": "%s",
                  "origin": "%s"
                }
                """.formatted(
                checkpoint.checkpointDate(),
                checkpoint.targetValue().toPlainString(),
                checkpoint.note(),
                checkpoint.origin()
        );
    }
}
