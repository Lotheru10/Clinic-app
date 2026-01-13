package pl.edu.agh.to.clinicapp.exception;

public class DoctorHasShiftException extends RuntimeException {
    public DoctorHasShiftException(int id) {
        super("Doctor with ID " + id + " has shift(s)");
    }
}
