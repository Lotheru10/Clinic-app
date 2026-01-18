package pl.edu.agh.to.clinicapp.dto.appointment_dto;

import java.time.LocalDateTime;

public record AvailableAppointmentSlotDTO(
        int shiftId,
        int doctorId,
        int officeId,
        LocalDateTime start,
        LocalDateTime end
) {
}
