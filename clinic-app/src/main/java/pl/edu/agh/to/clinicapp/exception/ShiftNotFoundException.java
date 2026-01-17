package pl.edu.agh.to.clinicapp.exception;

public class ShiftNotFoundException extends RuntimeException {
    public ShiftNotFoundException(int id) {
        super("Shift with ID"+ id+" not found");
    }
}
