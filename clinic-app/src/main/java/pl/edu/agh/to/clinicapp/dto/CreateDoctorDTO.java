package pl.edu.agh.to.clinicapp.dto;

public record CreateDoctorDTO(
        String firstName,
        String lastName,
        String peselNumber,
        String specialization,
        String address
) {
}
