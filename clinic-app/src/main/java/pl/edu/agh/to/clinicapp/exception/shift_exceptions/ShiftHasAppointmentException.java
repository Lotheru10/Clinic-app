package pl.edu.agh.to.clinicapp.exception.shift_exceptions;

public class ShiftHasAppointmentException extends RuntimeException {
    public ShiftHasAppointmentException(int id) {
        super("Shift with ID: " + id + "has appointment(s)");
    }
}
