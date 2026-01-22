package pl.edu.agh.to.clinicapp.dto.opinion_dto;

import java.time.LocalDateTime;

public record OpinionDTO(
        int rate,
        String message,
        String patientName,
        LocalDateTime date
) {
}
