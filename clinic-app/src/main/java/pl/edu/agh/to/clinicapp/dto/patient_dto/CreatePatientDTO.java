package pl.edu.agh.to.clinicapp.dto.patient_dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreatePatientDTO(
        @NotBlank(message = "First name is required")
        String firstName,
        @NotBlank(message = "Last name is required")
        String lastName,
        @NotBlank(message = "PESEL is required")
        @Pattern(regexp = "[0-9]{11}")
        String peselNumber,
        @NotBlank(message = "Address is required")
        String address
) {
}
