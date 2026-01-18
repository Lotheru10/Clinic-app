package pl.edu.agh.to.clinicapp.exception.doctor_exceptions;

public class DoctorHasShiftException extends RuntimeException {
    public DoctorHasShiftException(int id) {
        super("Doctor with ID " + id + " has shift(s)");
    }
}
