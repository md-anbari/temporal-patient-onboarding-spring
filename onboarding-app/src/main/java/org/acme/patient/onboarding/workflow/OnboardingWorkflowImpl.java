package org.acme.patient.onboarding.workflow;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import lombok.RequiredArgsConstructor;
import org.acme.patient.onboarding.model.Doctor;
import org.acme.patient.onboarding.model.Hospital;
import org.acme.patient.onboarding.model.Patient;
import org.acme.patient.onboarding.service.ServiceExecutor;

import java.time.Duration;

@RequiredArgsConstructor
public class OnboardingWorkflowImpl implements OnboardingWorkflow {

    private final ServiceExecutor serviceExecutor;
    private Patient patient;

    @Override
    public Patient startOnboarding(Patient patient) {
        this.patient = patient;

        // Assign hospital
        Hospital hospital = serviceExecutor.assignHospitalToPatient(patient.getZip());
        patient.setHospital(hospital);

        // Assign doctor
        Doctor doctor = serviceExecutor.assignDoctorToPatient(patient.getCondition());
        patient.setDoctor(doctor);

        // Notify patient
        if ("EMAIL".equals(patient.getContactMethod())) {
            serviceExecutor.notifyViaEmail(patient.getEmail(), "Your onboarding is complete");
        } else {
            serviceExecutor.notifyViaText(patient.getPhone(), "Your onboarding is complete");
        }

        patient.setOnboarded("yes");
        return patient;
    }

    @Override
    public Patient getStatus() {
        return patient;
    }
} 