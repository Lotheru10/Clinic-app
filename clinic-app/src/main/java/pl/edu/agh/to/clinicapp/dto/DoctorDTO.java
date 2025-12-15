package pl.edu.agh.to.clinicapp.dto;

public record DoctorDTO(
        int id,
        String firstName,
        String lastName,
        String specialization
) {
}
