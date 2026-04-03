package com.ybritto.milestory.infrastructure.api;

import com.ybritto.milestory.application.status.GetFoundationStatusUseCase;
import com.ybritto.milestory.domain.status.FoundationStatus;
import com.ybritto.milestory.generated.api.FoundationStatusApi;
import com.ybritto.milestory.generated.model.FoundationStatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class FoundationStatusController implements FoundationStatusApi {

    private final GetFoundationStatusUseCase getFoundationStatusUseCase;
    private final FoundationStatusResponseMapper foundationStatusResponseMapper;

    @Override
    public ResponseEntity<FoundationStatusResponse> getFoundationStatus() {
        FoundationStatus status = getFoundationStatusUseCase.getStatus();
        log.debug("Serving foundation status for API version {}", status.apiVersion());
        return ResponseEntity.ok(foundationStatusResponseMapper.toResponse(status));
    }
}
