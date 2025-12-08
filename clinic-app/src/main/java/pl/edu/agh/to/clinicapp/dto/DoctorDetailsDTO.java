package pl.edu.agh.to.clinicapp.dto;

public record DoctorDetailsDTO(
        int id,
        String firstName,
        String lastName,
        String specialization,
        String address
) {}
