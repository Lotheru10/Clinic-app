package pl.edu.agh.to.clinicapp.dto.doctors_office_dto;

import jakarta.validation.constraints.NotBlank;

public record CreateDoctorsOfficeDTO(
        @NotBlank(message = "Doctor's office room number is required")
        String roomNumber,
        @NotBlank(message = "Doctor's office room description is required")
        String roomDescription
) {
}
