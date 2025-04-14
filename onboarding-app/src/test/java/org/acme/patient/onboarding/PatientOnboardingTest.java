package org.acme.patient.onboarding;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;
import org.acme.patient.onboarding.app.Onboarding;
import org.acme.patient.onboarding.app.OnboardingImpl;
import org.acme.patient.onboarding.app.ServiceExecutor;
import org.acme.patient.onboarding.model.Doctor;
import org.acme.patient.onboarding.model.Hospital;
import org.acme.patient.onboarding.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PatientOnboardingTest {

    private TestWorkflowEnvironment testEnv;
    private Worker worker;
    private WorkflowClient client;

    @MockBean
    private ServiceExecutor serviceExecutor;

    @BeforeEach
    void setUp() {
        testEnv = TestWorkflowEnvironment.newInstance();
        worker = testEnv.newWorker("OnboardingTaskQueue");
        worker.registerWorkflowImplementationTypes(OnboardingImpl.class);
        client = testEnv.getWorkflowClient();

        // Mock activity responses with proper Hospital and Doctor objects
        Hospital mockHospital = Hospital.builder()
                .name("Test Hospital")
                .address("123 Test St")
                .phone("555-123-4567")
                .zip("30040")
                .build();

        Doctor mockDoctor = Doctor.builder()
                .name("Dr. Test")
                .specialty("Cancer")
                .phone("555-987-6543")
                .email("dr.test@hospital.com")
                .build();

        when(serviceExecutor.assignHospitalToPatient(any())).thenReturn(mockHospital);
        when(serviceExecutor.assignDoctorToPatient(any())).thenReturn(mockDoctor);

        testEnv.start();
    }

    @Test
    void testOnboardingWorkflow() {
        Patient patient = new Patient();
        patient.setId("123");
        patient.setName("John Doe");
        patient.setEmail("john@example.com");
        patient.setPhone("555-55-5555");
        patient.setZip("30040");
        patient.setCondition("Cancer");
        patient.setContactMethod("PHONE");

        WorkflowOptions options = WorkflowOptions.newBuilder()
            .setTaskQueue("OnboardingTaskQueue")
            .setWorkflowId("test-workflow-" + patient.getId())
            .build();

        Onboarding workflow = client.newWorkflowStub(
            Onboarding.class,
            options
        );

        Patient result = workflow.onboardNewPatient(patient);

        assertNotNull(result);
        assertEquals("yes", result.getOnboarded());

        verify(serviceExecutor).assignHospitalToPatient(patient.getZip());
        verify(serviceExecutor).assignDoctorToPatient(patient.getCondition());
        verify(serviceExecutor).notifyViaText(patient.getPhone(), "Your onboarding is complete");
    }
}
