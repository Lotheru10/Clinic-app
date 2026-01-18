package pl.edu.agh.to.clinicapp.appointment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.agh.to.clinicapp.doctor.Doctor;
import pl.edu.agh.to.clinicapp.doctors_office.DoctorsOffice;
import pl.edu.agh.to.clinicapp.dto.appointment_dto.AppointmentDTO;
import pl.edu.agh.to.clinicapp.dto.appointment_dto.CreateAppointmentDTO;
import pl.edu.agh.to.clinicapp.exception.AppointmentSlotTakenException;
import pl.edu.agh.to.clinicapp.exception.PatientHasAppointmentException;
import pl.edu.agh.to.clinicapp.patient.Patient;
import pl.edu.agh.to.clinicapp.patient.PatientRepository;
import pl.edu.agh.to.clinicapp.shift.Shift;
import pl.edu.agh.to.clinicapp.shift.ShiftRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock private AppointmentRepository appointmentRepository;
    @Mock private ShiftRepository shiftRepository;
    @Mock private PatientRepository patientRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    private Patient patient;
    private Shift shift;
    private Doctor doctor;
    private DoctorsOffice office;

    @BeforeEach
    void setUp() {
        patient = new Patient();
        patient.setFirstName("Mateusz");
        patient.setLastName("Nowak");
        patient.setId(1);

        doctor = new Doctor();
        doctor.setFirstName("Jan");
        doctor.setLastName("Kowalski");

        office = new DoctorsOffice();
        office.setRoomNumber("101");

        shift = new Shift();
        shift.setId(1);
        shift.setStart(LocalDateTime.of(2026, 1, 1, 10, 0));
        shift.setEnd(LocalDateTime.of(2026, 1, 1, 14, 0));
        shift.setDoctor(doctor);
        shift.setOffice(office);
    }

    @Test
    void shouldAddAppointmentSuccessfully() {
        // given
        LocalDateTime appointmentStart = LocalDateTime.of(2026, 1, 1, 10, 0);
        CreateAppointmentDTO dto = new CreateAppointmentDTO(1, 1, appointmentStart);

        when(patientRepository.findById(1)).thenReturn(Optional.of(patient));
        when(shiftRepository.findById(1)).thenReturn(Optional.of(shift));
        when(appointmentRepository.patientBusy(anyInt(), any(), any())).thenReturn(false);
        when(appointmentRepository.shiftBusy(anyInt(), any(), any())).thenReturn(false);

        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(invocation -> {
            Appointment a = invocation.getArgument(0);
            a.setId(100);
            return a;
        });

        // when
        AppointmentDTO result = appointmentService.addAppointment(dto);

        // then
        assertNotNull(result);
        assertEquals(100, result.id());
        assertEquals(appointmentStart, result.start());
        assertEquals(appointmentStart.plusMinutes(15), result.end());
        verify(appointmentRepository).save(any(Appointment.class));
    }

    @Test
    void shouldThrowExceptionWhenTimeIsOutsideShift() {
        // given-próba wizyty o 15 (shift kończy się o 14)
        LocalDateTime appointmentStart = LocalDateTime.of(2026, 1, 1, 15, 0);
        CreateAppointmentDTO dto = new CreateAppointmentDTO(1, 1, appointmentStart);

        when(patientRepository.findById(1)).thenReturn(Optional.of(patient));
        when(shiftRepository.findById(1)).thenReturn(Optional.of(shift));

        // when & then
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> appointmentService.addAppointment(dto));
        assertEquals("Appointment outside of shift", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPatientIsBusy() {
        // given
        LocalDateTime appointmentStart = LocalDateTime.of(2026, 1, 1, 11, 0);
        CreateAppointmentDTO dto = new CreateAppointmentDTO(1, 1, appointmentStart);

        when(patientRepository.findById(1)).thenReturn(Optional.of(patient));
        when(shiftRepository.findById(1)).thenReturn(Optional.of(shift));
        when(appointmentRepository.patientBusy(eq(1), any(), any())).thenReturn(true);

        // when & then
        assertThrows(PatientHasAppointmentException.class, () -> appointmentService.addAppointment(dto));
    }

    @Test
    void shouldThrowExceptionWhenShiftSlotIsTaken() {
        // given
        LocalDateTime appointmentStart = LocalDateTime.of(2026, 1, 1, 11, 0);
        CreateAppointmentDTO dto = new CreateAppointmentDTO(1, 1, appointmentStart);

        when(patientRepository.findById(1)).thenReturn(Optional.of(patient));
        when(shiftRepository.findById(1)).thenReturn(Optional.of(shift));
        when(appointmentRepository.patientBusy(anyInt(), any(), any())).thenReturn(false);
        when(appointmentRepository.shiftBusy(eq(1), any(), any())).thenReturn(true);

        // when & then
        assertThrows(AppointmentSlotTakenException.class, () -> appointmentService.addAppointment(dto));
    }
}