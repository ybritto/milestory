package com.ybritto.milestory.status.application.model;

public record FoundationRuntimeStatus(
        String applicationName,
        String activeProfile,
        String databaseName,
        String databaseStatus,
        String migrationStatus,
        String baseline
) {
}
