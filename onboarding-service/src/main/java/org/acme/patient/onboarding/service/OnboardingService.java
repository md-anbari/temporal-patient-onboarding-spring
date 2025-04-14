package org.acme.patient.onboarding.service;

import lombok.RequiredArgsConstructor;
import org.acme.patient.onboarding.model.Doctor;
import org.acme.patient.onboarding.model.Hospital;
import org.acme.patient.onboarding.repository.DoctorRepository;
import org.acme.patient.onboarding.repository.HospitalRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OnboardingService {
    private final HospitalRepository hospitalRepository;
    private final DoctorRepository doctorRepository;

    public Hospital assignHospitalToPatient(String zip) {
        List<Hospital> hospitals = hospitalRepository.findByZip(zip);
        return hospitals.stream()
                .findFirst()
                .orElseGet(() -> createDefaultHospital(zip));
    }

    public Doctor assignDoctorToPatient(String condition) {
        List<Doctor> doctors = doctorRepository.findBySpecialty(condition);
        return doctors.stream()
                .findFirst()
                .orElseGet(() -> createDefaultDoctor(condition));
    }

    private Hospital createDefaultHospital(String zip) {
        Hospital defaultHospital = Hospital.builder()
                .name("Local Hospital")
                .address("123 Local Street")
                .phone("555-55-5555")
                .zip(zip)
                .build();
        return hospitalRepository.save(defaultHospital);
    }

    private Doctor createDefaultDoctor(String condition) {
        Doctor defaultDoctor = Doctor.builder()
                .name("Michael Scott")
                .specialty(condition)
                .phone("555-55-5555")
                .email("michael.scott@hospital.com")
                .build();
        return doctorRepository.save(defaultDoctor);
    }
} 