package pl.edu.agh.to.clinicapp.dto.patient_dto;

public record PatientDetailsDTO(
        int id,
        String firstName,
        String lastName,
        String address
) {}
