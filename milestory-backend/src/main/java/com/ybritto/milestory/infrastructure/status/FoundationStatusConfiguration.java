package com.ybritto.milestory.infrastructure.status;

import com.ybritto.milestory.application.status.FoundationRuntimeStatusProvider;
import com.ybritto.milestory.application.status.GetFoundationStatusUseCase;
import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class FoundationStatusConfiguration {

    @Bean
    Clock foundationStatusClock() {
        return Clock.systemUTC();
    }

    @Bean
    GetFoundationStatusUseCase getFoundationStatusUseCase(
            FoundationRuntimeStatusProvider runtimeStatusProvider,
            Clock foundationStatusClock
    ) {
        return new GetFoundationStatusUseCase(runtimeStatusProvider, foundationStatusClock);
    }
}
