package pl.edu.agh.to.clinicapp.exception.patient_exceptions;

public class PatientNotFoundException extends RuntimeException {
    public PatientNotFoundException(int id) {
        super("Patient with ID " + id + " not found");
    }
}
