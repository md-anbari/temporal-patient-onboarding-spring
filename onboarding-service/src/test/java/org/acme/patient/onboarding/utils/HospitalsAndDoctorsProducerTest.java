package org.acme.patient.onboarding.utils;

import org.acme.patient.onboarding.model.Doctor;
import org.acme.patient.onboarding.model.Hospital;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HospitalsAndDoctorsProducerTest {

    @Autowired
    private List<Hospital> hospitals;

    @Autowired
    private List<Doctor> doctors;

    @Test
    void testParticipatingHospitals() {
        assertNotNull(hospitals);
        assertEquals(5, hospitals.size());
        
        Hospital firstHospital = hospitals.get(0);
        assertEquals("Northside Hospital", firstHospital.getName());
        assertEquals("30041", firstHospital.getZip());
    }

    @Test
    void testParticipatingDoctors() {
        assertNotNull(doctors);
        assertEquals(4, doctors.size());
        
        Doctor firstDoctor = doctors.get(0);
        assertEquals("Dr. John Doe", firstDoctor.getName());
        assertEquals("Diabetes", firstDoctor.getSpecialty());
    }
} 