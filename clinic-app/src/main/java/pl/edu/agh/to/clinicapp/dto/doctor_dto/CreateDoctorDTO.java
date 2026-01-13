package pl.edu.agh.to.clinicapp.dto.doctor_dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import pl.edu.agh.to.clinicapp.doctor.Specialization;

public record CreateDoctorDTO(
        @NotBlank(message = "First name is required")
        String firstName,
        @NotBlank(message = "Last name is required")
        String lastName,
        @NotBlank(message = "PESEL is required")
        @Pattern(regexp = "[0-9]{11}")
        String peselNumber,
        @NotNull(message = "Specialization is required")
        Specialization specialization,
        @NotBlank(message = "Address is required")
        String address
) {
}
