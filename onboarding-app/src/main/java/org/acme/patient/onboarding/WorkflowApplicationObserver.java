package org.acme.patient.onboarding;

import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import org.acme.patient.onboarding.app.OnboardingImpl;
import org.acme.patient.onboarding.app.ServiceExecutorImpl;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class WorkflowApplicationObserver {

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationEvent() {
        // Get a Workflow service stub.
        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();

        // Get a Workflow service client
        WorkflowClient client = WorkflowClient.newInstance(service);

        // Create a Worker factory that can be used to create Workers that poll specific Task Queues.
        WorkerFactory factory = WorkerFactory.newInstance(client);

        // Create a Worker that polls the Task Queue.
        Worker worker = factory.newWorker("OnboardingTaskQueue");

        // Register Workflow implementation classes
        worker.registerWorkflowImplementationTypes(OnboardingImpl.class);

        // Register Activity implementation classes
        worker.registerActivitiesImplementations(new ServiceExecutorImpl());

        // Start polling the Task Queue.
        factory.start();
    }
}
