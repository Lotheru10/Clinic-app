package pl.edu.agh.to.clinicapp.dto;

public record DoctorDetailsDTO(
        String firstName,
        String lastName,
        String specialization,
        String address
) {}
