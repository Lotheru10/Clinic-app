package pl.edu.agh.to.clinicapp.dto.opinion_dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateOpinionDTO(
        @NotNull(message = "Rate is required")
        @Min(value = 1, message = "Rate must be at least 1")
        @Max(value = 5, message = "Rate must be at most 5")
        int rate,
        String message,
        @NotNull(message = "Appointment ID is required")
        int appointmentId
        ) {
}
