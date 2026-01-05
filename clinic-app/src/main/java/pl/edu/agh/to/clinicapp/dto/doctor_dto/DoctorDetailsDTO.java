package pl.edu.agh.to.clinicapp.dto.doctor_dto;

import pl.edu.agh.to.clinicapp.doctor.Specialization;

public record DoctorDetailsDTO(
        int id,
        String firstName,
        String lastName,
        Specialization specialization,
        String address
) {}
