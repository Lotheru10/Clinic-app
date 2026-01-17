package pl.edu.agh.to.clinicapp.exception;

public class PatientHasAppointmentException extends RuntimeException {
    public PatientHasAppointmentException(int id) {
        super("Patient with ID" + id + "has appointment at given time");
    }
}
