package com.ybritto.milestory.status.application.port.out;

import com.ybritto.milestory.status.application.model.FoundationRuntimeStatus;

public interface FoundationRuntimeStatusPort {

    FoundationRuntimeStatus getCurrentStatus();
}
