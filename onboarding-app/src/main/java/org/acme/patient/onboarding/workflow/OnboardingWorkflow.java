package org.acme.patient.onboarding.workflow;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import io.temporal.workflow.QueryMethod;
import org.acme.patient.onboarding.model.Patient;

@WorkflowInterface
public interface OnboardingWorkflow {
    @WorkflowMethod
    Patient startOnboarding(Patient patient);

    @QueryMethod
    Patient getStatus();
} 