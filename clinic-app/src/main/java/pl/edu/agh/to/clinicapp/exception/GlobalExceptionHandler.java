package pl.edu.agh.to.clinicapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.edu.agh.to.clinicapp.exception.appointment_exceptions.AppointmentSlotTakenException;
import pl.edu.agh.to.clinicapp.exception.doctor_exceptions.DoctorHasShiftException;
import pl.edu.agh.to.clinicapp.exception.doctor_exceptions.DoctorNotFoundException;
import pl.edu.agh.to.clinicapp.exception.doctor_office_exceptions.DoctorsOfficeHasShiftException;
import pl.edu.agh.to.clinicapp.exception.doctor_office_exceptions.DoctorsOfficeNotFoundException;
import pl.edu.agh.to.clinicapp.exception.patient_exceptions.PatientHasAppointmentException;
import pl.edu.agh.to.clinicapp.exception.patient_exceptions.PatientNotFoundException;
import pl.edu.agh.to.clinicapp.exception.shift_exceptions.ShiftNotFoundException;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(DoctorNotFoundException.class)
    public String handleDoctorNotFound(DoctorNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(DoctorsOfficeNotFoundException.class)
    public String handleDoctorsOfficeNotFound(DoctorsOfficeNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(PatientNotFoundException.class)
    public String handlePatientNotFound(PatientNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationExceptions(MethodArgumentNotValidException ex) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DoctorHasShiftException.class)
    public ResponseEntity<Object> handleDoctorHasShift(DoctorHasShiftException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("message", ex.getMessage()));
    }
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DoctorsOfficeHasShiftException.class)
    public ResponseEntity<Object> handleDoctorsOfficeHasShift(DoctorsOfficeHasShiftException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("message", ex.getMessage()));
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(PatientHasAppointmentException.class)
    public ResponseEntity<Object> handlePatientHasAppointment(PatientHasAppointmentException ex){
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("message", ex.getMessage()));
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AppointmentSlotTakenException.class)
    public ResponseEntity<Object> handleAppointmentSlotTaken(AppointmentSlotTakenException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("message", ex.getMessage()));
    }

    // 3. Obsługa braku Shift (używane w AppointmentService)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ShiftNotFoundException.class)
    public ResponseEntity<Object> handleShiftNotFound(ShiftNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", ex.getMessage()));
    }

    // 4. Obsługa IllegalArgumentException (np. wizyta poza godzinami shiftu)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", ex.getMessage()));
    }

}
