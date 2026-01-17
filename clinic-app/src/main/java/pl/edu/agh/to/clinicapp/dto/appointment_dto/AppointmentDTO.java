package pl.edu.agh.to.clinicapp.dto.appointment_dto;

import java.time.LocalDateTime;

public record AppointmentDTO(
        int id,
        String doctorName,
        String officeName,
        LocalDateTime start,
        LocalDateTime end
) {
}
