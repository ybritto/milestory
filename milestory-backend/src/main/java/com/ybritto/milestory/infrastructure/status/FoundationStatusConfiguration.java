package com.ybritto.milestory.infrastructure.status;

import com.ybritto.milestory.application.status.FoundationRuntimeStatusProvider;
import com.ybritto.milestory.application.status.GetFoundationStatusUseCase;
import java.time.Clock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
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
        log.debug("Creating GetFoundationStatusUseCase bean");
        return new GetFoundationStatusUseCase(runtimeStatusProvider, foundationStatusClock);
    }
}
