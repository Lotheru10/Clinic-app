package pl.edu.agh.to.clinicapp.exception;

import java.time.LocalDateTime;

public class AppointmentSlotTakenException extends RuntimeException {
    public AppointmentSlotTakenException(int shiftId, LocalDateTime start) {
        super("Slot already taken for shiftId " + shiftId + " at " + start);
    }
}
