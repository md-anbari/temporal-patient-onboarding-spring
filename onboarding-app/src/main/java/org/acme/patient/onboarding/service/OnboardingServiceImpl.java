package org.acme.patient.onboarding.service;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import lombok.RequiredArgsConstructor;
import org.acme.patient.onboarding.app.Onboarding;
import org.acme.patient.onboarding.model.Patient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OnboardingServiceImpl implements OnboardingService {

    private final WorkflowClient workflowClient;

    @Override
    public Patient startOnboarding(Patient patient) {
        WorkflowOptions options = WorkflowOptions.newBuilder()
                .setTaskQueue("OnboardingTaskQueue")
                .setWorkflowId(patient.getId())
                .build();

        Onboarding workflow = workflowClient.newWorkflowStub(Onboarding.class, options);
        return workflow.onboardNewPatient(patient);
    }

    @Override
    public Patient getStatus(String id) {
        Onboarding workflow = workflowClient.newWorkflowStub(Onboarding.class, id);
        String status = workflow.getStatus();
        // Since Onboarding.getStatus() returns String, we'll create a new Patient with the status
        return Patient.builder().id(id).onboarded(status).build();
    }
} 