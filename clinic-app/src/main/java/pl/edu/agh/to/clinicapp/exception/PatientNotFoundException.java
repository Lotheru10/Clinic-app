package pl.edu.agh.to.clinicapp.exception;

public class PatientNotFoundException extends RuntimeException {
    public PatientNotFoundException(int id) {
        super("Patient with ID " + id + " not found");
    }
}
