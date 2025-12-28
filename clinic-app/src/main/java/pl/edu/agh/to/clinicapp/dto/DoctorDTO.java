package pl.edu.agh.to.clinicapp.dto;

import pl.edu.agh.to.clinicapp.doctor.Specialization;

public record DoctorDTO(
        int id,
        String firstName,
        String lastName,
        Specialization specialization
) {
}
