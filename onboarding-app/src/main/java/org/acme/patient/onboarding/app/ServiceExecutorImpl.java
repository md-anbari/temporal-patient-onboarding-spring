package org.acme.patient.onboarding.app;

import org.acme.patient.onboarding.model.Doctor;
import org.acme.patient.onboarding.model.Hospital;
import org.springframework.stereotype.Component;

@Component
public class ServiceExecutorImpl implements ServiceExecutor {

    @Override
    public Hospital assignHospitalToPatient(String zip) {
        return Hospital.builder()
                .name("Test Hospital")
                .address("123 Test St")
                .phone("555-123-4567")
                .zip(zip)
                .build();
    }

    @Override
    public Doctor assignDoctorToPatient(String condition) {
        return Doctor.builder()
                .name("Dr. Test")
                .specialty(condition)
                .phone("555-987-6543")
                .email("dr.test@hospital.com")
                .build();
    }

    @Override
    public void notifyViaEmail(String email, String message) {
        System.out.println("Sending email to " + email + ": " + message);
    }

    @Override
    public void notifyViaText(String phone, String message) {
        System.out.println("Sending text to " + phone + ": " + message);
    }
}
