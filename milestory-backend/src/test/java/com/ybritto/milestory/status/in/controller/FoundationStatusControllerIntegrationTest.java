package com.ybritto.milestory.status.in.controller;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ybritto.milestory.status.application.model.FoundationRuntimeStatus;
import com.ybritto.milestory.status.application.port.out.FoundationRuntimeStatusPort;
import com.ybritto.milestory.status.application.usecase.GetFoundationStatusUseCase;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class FoundationStatusControllerIntegrationTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        FoundationStatusController controller = new FoundationStatusController(
                new GetFoundationStatusUseCase(
                        (FoundationRuntimeStatusPort) () -> new FoundationRuntimeStatus(
                                "Milestory",
                                "test",
                                "milestory",
                                "connected",
                                "applied",
                                "001-foundation-baseline"
                        ),
                        Clock.fixed(Instant.parse("2026-04-04T00:00:00Z"), ZoneOffset.UTC)
                ),
                Mappers.getMapper(FoundationStatusResponseMapper.class)
        );

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void anonymousRequestReturnsFoundationStatus() throws Exception {
        mockMvc.perform(get("/api/v1/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mode", is("ready")))
                .andExpect(jsonPath("$.database.status", is("connected")))
                .andExpect(jsonPath("$.database.name", is("milestory")))
                .andExpect(jsonPath("$.migration.baseline", is("001-foundation-baseline")))
                .andExpect(jsonPath("$.generatedAt", notNullValue()));
    }
}
