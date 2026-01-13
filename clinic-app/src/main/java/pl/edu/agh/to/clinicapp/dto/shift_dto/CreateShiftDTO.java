package pl.edu.agh.to.clinicapp.dto.shift_dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import pl.edu.agh.to.clinicapp.doctor.Doctor;
import pl.edu.agh.to.clinicapp.doctor.Specialization;
import pl.edu.agh.to.clinicapp.doctors_office.DoctorsOffice;

import java.time.LocalDateTime;

public record CreateShiftDTO(
        @NotNull(message = "Doctor is required")
        int doctorId,
        @NotNull(message = "Doctor's office is required")
        int officeId,
        @NotNull(message = "Start is required")
        LocalDateTime start,
        @NotNull(message = "End is required")
        LocalDateTime end
) {
}
