package org.acme.patient.onboarding.config;

import io.temporal.testing.TestWorkflowEnvironment;
import org.acme.patient.onboarding.service.ServiceExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import static org.mockito.Mockito.mock;

@Configuration
public class TestConfig {

    @Bean
    @Primary
    public TestWorkflowEnvironment testWorkflowEnvironment() {
        return TestWorkflowEnvironment.newInstance();
    }

    @Bean
    @Primary
    public ServiceExecutor serviceExecutor() {
        return mock(ServiceExecutor.class);
    }
} 