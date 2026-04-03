package com.ybritto.milestory.status.in.controller;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.ybritto.milestory.status.domain.FoundationStatus;
import com.ybritto.milestory.status.domain.FoundationStatusMode;
import com.ybritto.milestory.generated.model.FoundationStatusResponse;
import com.ybritto.milestory.generated.model.FoundationStatusResponseMode;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class FoundationStatusResponseMapperTest {

    private final FoundationStatusResponseMapper mapper =
            Mappers.getMapper(FoundationStatusResponseMapper.class);

    @Test
    void mapsDomainStatusToGeneratedApiResponse() {
        FoundationStatus status = new FoundationStatus(
                "Milestory foundation is ready",
                "The auth-free Phase 1 stack is running.",
                FoundationStatusMode.READY,
                "v1",
                new FoundationStatus.DatabaseStatus("connected", "milestory"),
                new FoundationStatus.MigrationStatus("applied", "001-foundation-baseline"),
                Instant.parse("2026-04-04T00:00:00Z"),
                List.of("Active profile: default", "Authentication is intentionally deferred in Phase 1.")
        );

        FoundationStatusResponse response = mapper.toResponse(status);

        assertEquals("Milestory foundation is ready", response.headline());
        assertEquals(FoundationStatusResponseMode.READY, response.mode());
        assertEquals("connected", response.database().status());
        assertEquals("milestory", response.database().name());
        assertEquals("001-foundation-baseline", response.migration().baseline());
        assertEquals(OffsetDateTime.ofInstant(status.generatedAt(), ZoneOffset.UTC), response.generatedAt());
        assertArrayEquals(status.notes().toArray(String[]::new), response.notes());
    }
}
