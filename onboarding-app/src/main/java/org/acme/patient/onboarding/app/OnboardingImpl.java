package org.acme.patient.onboarding.app;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import org.acme.patient.onboarding.model.Patient;
import org.acme.patient.onboarding.utils.ActivityStubUtils;

import java.time.Duration;

public class OnboardingImpl implements Onboarding {

    ServiceExecutor serviceExecutor;
    String status;
    Patient onboardingPatient;

    public OnboardingImpl() {
        ActivityOptions options = ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofSeconds(10))
            .build();
        serviceExecutor = Workflow.newActivityStub(ServiceExecutor.class, options);
    }

    @Override
    public Patient onboardNewPatient(Patient patient) {
        onboardingPatient = patient;

        try {
            // 1. assign hospital to patient
            status = "Assigning hospital to patient: " + onboardingPatient.getName();
            System.out.println(status);
            onboardingPatient.setHospital(
                    serviceExecutor.assignHospitalToPatient(onboardingPatient.getZip()));

            // 2. assign doctor to patient
            status = "Assigning doctor to patient: " + onboardingPatient.getName();
            System.out.println(status);
            onboardingPatient.setDoctor(
                    serviceExecutor.assignDoctorToPatient(onboardingPatient.getCondition()));

            // 3. notify patient with preferred contact method
            status = "Notifying patient: " + onboardingPatient.getName();
            System.out.println(status);
            if ("EMAIL".equals(onboardingPatient.getContactMethod())) {
                serviceExecutor.notifyViaEmail(onboardingPatient.getEmail(), "Your onboarding is complete");
            } else if ("PHONE".equals(onboardingPatient.getContactMethod())) {
                serviceExecutor.notifyViaText(onboardingPatient.getPhone(), "Your onboarding is complete");
            }

            // 4. finalize onboarding
            status = "Finalizing onboarding for: " + onboardingPatient.getName();
            System.out.println(status);
            onboardingPatient.setOnboarded("yes");
            System.out.println("Onboarding completed successfully");

        } catch (Exception e) {
            System.out.println("Error during onboarding: " + e.getMessage());
            e.printStackTrace();
            onboardingPatient.setOnboarded("no");
        }

        return onboardingPatient;
    }

    @Override
    public String getStatus() {
        return status;
    }

}
