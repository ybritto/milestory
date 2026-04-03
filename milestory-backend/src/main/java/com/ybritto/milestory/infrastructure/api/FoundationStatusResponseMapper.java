package com.ybritto.milestory.infrastructure.api;

import com.ybritto.milestory.domain.status.FoundationStatus;
import com.ybritto.milestory.domain.status.FoundationStatusMode;
import com.ybritto.milestory.generated.model.FoundationStatusResponse;
import com.ybritto.milestory.generated.model.FoundationStatusResponseMode;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ValueMapping;

@Mapper(componentModel = "spring")
public interface FoundationStatusResponseMapper {

    @Mapping(target = "generatedAt", source = "generatedAt", qualifiedByName = "instantToOffsetDateTime")
    @Mapping(target = "notes", source = "notes", qualifiedByName = "notesToArray")
    FoundationStatusResponse toResponse(FoundationStatus status);

    @ValueMapping(source = "READY", target = "READY")
    @ValueMapping(source = "EMPTY", target = "EMPTY")
    @ValueMapping(source = "DEGRADED", target = "DEGRADED")
    FoundationStatusResponseMode toResponseMode(FoundationStatusMode mode);

    @Named("instantToOffsetDateTime")
    default OffsetDateTime instantToOffsetDateTime(java.time.Instant generatedAt) {
        return generatedAt == null ? null : OffsetDateTime.ofInstant(generatedAt, ZoneOffset.UTC);
    }

    @Named("notesToArray")
    default String[] notesToArray(List<String> notes) {
        return notes == null ? new String[0] : notes.toArray(String[]::new);
    }
}
