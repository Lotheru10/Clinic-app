package pl.edu.agh.to.clinicapp.dto.opinion_dto;

import jakarta.validation.constraints.NotBlank;

public record CreateOpinionDTO(
        @NotBlank(message = "Rate is required")
        int rate,
        String message,
        @NotBlank(message = "Appointment ID is required")
        int appointmentId
        ) {
}
