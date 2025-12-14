package pl.edu.agh.to.clinicapp.exception;

public class DoctorNotFoundException extends RuntimeException{
    public DoctorNotFoundException(int id) {
        super("Doctor with ID " + id + "not found");
    }
}
