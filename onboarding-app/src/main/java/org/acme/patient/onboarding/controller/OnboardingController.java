package org.acme.patient.onboarding.controller;

import lombok.RequiredArgsConstructor;
import org.acme.patient.onboarding.model.Patient;
import org.acme.patient.onboarding.service.OnboardingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/onboarding")
@RequiredArgsConstructor
public class OnboardingController {

    private final OnboardingService onboardingService;

    @PostMapping("/start")
    public Patient startOnboarding(@RequestBody Patient patient) {
        return onboardingService.startOnboarding(patient);
    }

    @GetMapping("/status/{id}")
    public Patient getStatus(@PathVariable String id) {
        return onboardingService.getStatus(id);
    }
} 