package org.acme.patient.onboarding.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    private String id;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    private String age;
    
    @Pattern(regexp = "^\\d{5}(-\\d{4})?$", message = "Invalid ZIP code format")
    private String zip;
    
    private String insurance;
    private String insuranceId;
    private String condition;
    private Hospital hospital;
    private Doctor doctor;
    private String onboarded;
    
    @Pattern(regexp = "^\\d{3}-\\d{2}-\\d{4}$", message = "Invalid phone number format")
    private String phone;
    
    @Email(message = "Invalid email format")
    private String email;
    
    private String contactMethod;
}
