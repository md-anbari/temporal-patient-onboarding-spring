package org.acme.patient.onboarding.service;

import org.acme.patient.onboarding.model.Patient;

public interface OnboardingService {
    Patient startOnboarding(Patient patient);
    Patient getStatus(String id);
} 