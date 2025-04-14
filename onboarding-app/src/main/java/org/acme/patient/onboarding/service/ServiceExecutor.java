package org.acme.patient.onboarding.service;

import org.acme.patient.onboarding.model.Doctor;
import org.acme.patient.onboarding.model.Hospital;
import org.acme.patient.onboarding.model.Patient;

public interface ServiceExecutor {
    Hospital assignHospitalToPatient(String zip);
    Doctor assignDoctorToPatient(String condition);
    void notifyViaEmail(String email, String message);
    void notifyViaText(String phone, String message);
} 