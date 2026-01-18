package pl.edu.agh.to.clinicapp.dto.shift_dto;

import java.time.LocalDateTime;

public record DoctorShiftDTO(
        int id,
        int officeId,
        String officeName,
        LocalDateTime start,
        LocalDateTime end
) {
}
