package pl.edu.agh.to.clinicapp.exception;

public class DoctorsOfficeHasShiftException extends RuntimeException {
    public DoctorsOfficeHasShiftException(int id) {
        super("Doctor's office with ID "+ id +" has shift(s)");
    }
}
