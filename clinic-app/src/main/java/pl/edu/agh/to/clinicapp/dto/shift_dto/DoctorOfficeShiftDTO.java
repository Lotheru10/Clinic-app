package pl.edu.agh.to.clinicapp.dto.shift_dto;

import java.time.LocalDateTime;

public record DoctorOfficeShiftDTO(
        int id,
        int doctorId,
        String doctorName,
        LocalDateTime start,
        LocalDateTime end
) {
}
