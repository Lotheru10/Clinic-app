package pl.edu.agh.to.clinicapp.dto.shift_dto;

import java.time.LocalDateTime;

public record ShiftDTO(
        int id,
        int doctorId,
        String doctorName,
        int officeId,
        String officeName,
        LocalDateTime start,
        LocalDateTime end
) {
}
