package pl.edu.agh.to.clinicapp.exception.doctor_exceptions;

public class DoctorNotFoundException extends RuntimeException{
    public DoctorNotFoundException(int id) {
        super("Doctor with ID " + id + " not found");
    }
}
