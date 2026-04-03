package com.ybritto.milestory.status.out.adapter;

import com.ybritto.milestory.status.application.port.out.FoundationRuntimeStatusPort;
import com.ybritto.milestory.status.application.usecase.GetFoundationStatusUseCase;
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
            FoundationRuntimeStatusPort foundationRuntimeStatusPort,
            Clock foundationStatusClock
    ) {
        log.debug("Creating GetFoundationStatusUseCase bean");
        return new GetFoundationStatusUseCase(foundationRuntimeStatusPort, foundationStatusClock);
    }
}
