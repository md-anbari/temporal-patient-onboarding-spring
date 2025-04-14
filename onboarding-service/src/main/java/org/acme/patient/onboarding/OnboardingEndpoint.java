package org.acme.patient.onboarding;

import lombok.RequiredArgsConstructor;
import org.acme.patient.onboarding.model.Doctor;
import org.acme.patient.onboarding.model.Hospital;
import org.acme.patient.onboarding.service.OnboardingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Service;

@Service
@RestController
@RequestMapping("/onboard")
@Tag(name = "Onboarding Services Endpoints")
@RequiredArgsConstructor
public class OnboardingEndpoint {

    private final OnboardingService onboardingService;

    @PostMapping("/assignhospital")
    public synchronized Hospital assignHospitalToPatient(@RequestBody String zip) {
        return onboardingService.assignHospitalToPatient(zip);
    }

    @PostMapping("/assigndoctor")
    public synchronized Doctor assignDoctorToPatient(@RequestBody String condition) {
        return onboardingService.assignDoctorToPatient(condition);
    }

    @PostMapping("/notify")
    public synchronized void notifyPatient(@RequestBody String contact) {
        // do nothing here for demo...
        // irl would send email or text message or both
    }
}

