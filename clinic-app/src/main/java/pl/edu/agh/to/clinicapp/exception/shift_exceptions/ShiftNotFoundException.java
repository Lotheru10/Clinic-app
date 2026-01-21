package pl.edu.agh.to.clinicapp.exception.shift_exceptions;

public class ShiftNotFoundException extends RuntimeException {
    public ShiftNotFoundException(int id) {
        super("Shift with ID"+ id+" not found");
    }
}
