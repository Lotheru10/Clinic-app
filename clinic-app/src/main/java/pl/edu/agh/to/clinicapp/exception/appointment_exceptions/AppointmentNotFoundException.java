package pl.edu.agh.to.clinicapp.exception.appointment_exceptions;

public class AppointmentNotFoundException extends RuntimeException {
    public AppointmentNotFoundException(int id) {
        super("Appointment with ID "+ id+ " doesnt exist");
    }
}
