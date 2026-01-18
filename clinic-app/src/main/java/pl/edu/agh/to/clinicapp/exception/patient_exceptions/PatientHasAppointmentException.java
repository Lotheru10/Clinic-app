package pl.edu.agh.to.clinicapp.exception.patient_exceptions;

public class PatientHasAppointmentException extends RuntimeException {
    public PatientHasAppointmentException(int id) {
        super("Patient with ID" + id + "has appointment at given time");
    }
}
