package pl.edu.agh.to.clinicapp.dto.appointment_dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateAppointmentDTO(
        @NotNull(message = "Patient is required")
        int patientId,
        @NotNull(message = "Shift is required")
        int shiftId,
        @NotNull(message = "Start time is required")
        LocalDateTime start
) {

}
