package pl.edu.agh.to.clinicapp.appointment;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.edu.agh.to.clinicapp.dto.appointment_dto.AppointmentDTO;
import pl.edu.agh.to.clinicapp.dto.appointment_dto.CreateAppointmentDTO;
import pl.edu.agh.to.clinicapp.exception.appointment_exceptions.AppointmentSlotTakenException;
import pl.edu.agh.to.clinicapp.exception.patient_exceptions.PatientHasAppointmentException;
import pl.edu.agh.to.clinicapp.exception.patient_exceptions.PatientNotFoundException;
import pl.edu.agh.to.clinicapp.exception.shift_exceptions.ShiftNotFoundException;
import pl.edu.agh.to.clinicapp.patient.Patient;
import pl.edu.agh.to.clinicapp.patient.PatientRepository;
import pl.edu.agh.to.clinicapp.shift.Shift;
import pl.edu.agh.to.clinicapp.shift.ShiftRepository;

import java.time.LocalDateTime;

@Service
public class AppointmentService {
    private final static int DURATION = 15;
    private final AppointmentRepository appointmentRepository;
    private final ShiftRepository shiftRepository;
    private final PatientRepository patientRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, ShiftRepository shiftRepository, PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.shiftRepository = shiftRepository;
        this.patientRepository = patientRepository;
    }

    @Transactional
    public AppointmentDTO addAppointment(CreateAppointmentDTO createAppointmentDTO){
        int shiftId = createAppointmentDTO.shiftId();
        int patientId = createAppointmentDTO.patientId();
        LocalDateTime start = createAppointmentDTO.start();
        LocalDateTime end = start.plusMinutes(DURATION);

        Patient patient = patientRepository.findById(patientId).orElseThrow(
                () -> new PatientNotFoundException(patientId));

        Shift shift = shiftRepository.findById(shiftId).orElseThrow(
                () -> new ShiftNotFoundException(shiftId));

        if (start.isBefore(shift.getStart()) || end.isAfter(shift.getEnd())){
            throw new IllegalArgumentException("Appointment outside of shift");
        }
        if(appointmentRepository.patientBusy(patientId, start, end)){
            throw new PatientHasAppointmentException(patientId);
        }
        if(appointmentRepository.shiftBusy(shiftId, start, end)){
            throw new AppointmentSlotTakenException(shiftId, start);
        }
        Appointment appointment = new Appointment(shift, start, end, patient);

        Appointment saved = appointmentRepository.save(appointment);

        return new AppointmentDTO(
                saved.getId(),
                shift.getDoctor().getFirstName() + " " + shift.getDoctor().getLastName(),
                shift.getOffice().getRoomNumber(),
                start,
                end
        );





    }
}
