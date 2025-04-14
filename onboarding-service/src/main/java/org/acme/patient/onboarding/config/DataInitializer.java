package org.acme.patient.onboarding.config;

import org.acme.patient.onboarding.model.Doctor;
import org.acme.patient.onboarding.model.Hospital;
import org.acme.patient.onboarding.repository.DoctorRepository;
import org.acme.patient.onboarding.repository.HospitalRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(HospitalRepository hospitalRepository, DoctorRepository doctorRepository) {
        return args -> {
            // Create hospitals
            Hospital hospital1 = Hospital.builder()
                    .name("General Hospital")
                    .address("123 Main St")
                    .zip("30040")
                    .phone("555-123-4567")
                    .build();

            Hospital hospital2 = Hospital.builder()
                    .name("City Medical Center")
                    .address("456 Oak Ave")
                    .zip("30040")
                    .phone("555-987-6543")
                    .build();

            hospitalRepository.save(hospital1);
            hospitalRepository.save(hospital2);

            // Create doctors
            Doctor doctor1 = Doctor.builder()
                    .name("Dr. Smith")
                    .specialty("Oncology")
                    .phone("555-111-2222")
                    .email("dr.smith@hospital.com")
                    .build();

            Doctor doctor2 = Doctor.builder()
                    .name("Dr. Johnson")
                    .specialty("Cardiology")
                    .phone("555-333-4444")
                    .email("dr.johnson@hospital.com")
                    .build();

            doctorRepository.save(doctor1);
            doctorRepository.save(doctor2);
        };
    }
} 