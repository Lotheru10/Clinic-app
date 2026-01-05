package pl.edu.agh.to.clinicapp.exception;

public class DoctorsOfficeNotFoundException extends RuntimeException {
    public DoctorsOfficeNotFoundException(int id) {
        super("Doctor's office with id: " + id + "not found");
    }
}
