package org.acme.patient.onboarding.utils;

import org.acme.patient.onboarding.model.Doctor;
import org.acme.patient.onboarding.model.Hospital;
import org.acme.patient.onboarding.model.Patient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "onboarding-service", path = "/onboard")
public interface OnboardingServiceClient {

    @PostMapping("/assignhospital")
    Hospital assignHospitalToPatient(@RequestBody String zip);

    @PostMapping("/assigndoctor")
    Doctor assignDoctorToPatient(@RequestBody String condition);

    @PostMapping("/notify")
    Patient notifyPatient(@RequestBody String email);
}
