package pl.edu.agh.to.clinicapp.exception.appointment_exceptions;

public class AppointmentHasOpinionAlreadyException extends RuntimeException {
    public AppointmentHasOpinionAlreadyException(int id) {
        super("Appointment with ID "+ id +" already has an opinion.");
    }
}
