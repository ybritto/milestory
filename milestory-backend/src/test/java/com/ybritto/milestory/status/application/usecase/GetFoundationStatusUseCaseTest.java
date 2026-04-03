package com.ybritto.milestory.status.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ybritto.milestory.status.application.model.FoundationRuntimeStatus;
import com.ybritto.milestory.status.domain.FoundationStatus;
import com.ybritto.milestory.status.domain.FoundationStatusMode;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;

class GetFoundationStatusUseCaseTest {

    private static final Clock FIXED_CLOCK = Clock.fixed(Instant.parse("2026-04-04T00:00:00Z"), ZoneOffset.UTC);

    @Test
    void returnsReadyStatusWhenDatabaseAndBaselineAreHealthy() {
        GetFoundationStatusUseCase useCase = new GetFoundationStatusUseCase(
                () -> new FoundationRuntimeStatus(
                        "Milestory",
                        "test",
                        "milestory",
                        "connected",
                        "applied",
                        "001-foundation-baseline"
                ),
                FIXED_CLOCK
        );

        FoundationStatus status = useCase.getStatus();

        assertEquals(FoundationStatusMode.READY, status.mode());
        assertEquals("Milestory foundation is ready", status.headline());
        assertEquals("connected", status.database().status());
        assertEquals("applied", status.migration().status());
    }

    @Test
    void returnsEmptyStatusWhenBaselineIsStillPending() {
        GetFoundationStatusUseCase useCase = new GetFoundationStatusUseCase(
                () -> new FoundationRuntimeStatus(
                        "Milestory",
                        "test",
                        "milestory",
                        "connected",
                        "baseline-pending",
                        "001-foundation-baseline"
                ),
                FIXED_CLOCK
        );

        FoundationStatus status = useCase.getStatus();

        assertEquals(FoundationStatusMode.EMPTY, status.mode());
        assertEquals("baseline-pending", status.migration().status());
        assertTrue(status.notes().stream().anyMatch(note -> note.contains("pending")));
    }

    @Test
    void returnsDegradedStatusWhenDatabaseIsUnavailable() {
        GetFoundationStatusUseCase useCase = new GetFoundationStatusUseCase(
                () -> new FoundationRuntimeStatus(
                        "Milestory",
                        "test",
                        "milestory",
                        "unavailable",
                        "unavailable",
                        "001-foundation-baseline"
                ),
                FIXED_CLOCK
        );

        FoundationStatus status = useCase.getStatus();

        assertEquals(FoundationStatusMode.DEGRADED, status.mode());
        assertEquals("Milestory foundation is degraded", status.headline());
        assertTrue(status.notes().stream().anyMatch(note -> note.contains("health needs attention")));
    }
}
