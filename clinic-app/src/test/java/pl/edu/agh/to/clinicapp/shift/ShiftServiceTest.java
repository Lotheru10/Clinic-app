package pl.edu.agh.to.clinicapp.shift;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.agh.to.clinicapp.doctor.Doctor;
import pl.edu.agh.to.clinicapp.doctor.DoctorRepository;
import pl.edu.agh.to.clinicapp.doctor.Specialization;
import pl.edu.agh.to.clinicapp.doctors_office.DoctorsOffice;
import pl.edu.agh.to.clinicapp.doctors_office.DoctorsOfficeRepository;
import pl.edu.agh.to.clinicapp.dto.shift_dto.CreateShiftDTO;
import pl.edu.agh.to.clinicapp.dto.shift_dto.ShiftDTO;
import pl.edu.agh.to.clinicapp.exception.DoctorNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShiftServiceTest {

    @Mock
    private ShiftRepository shiftRepository;
    @Mock
    private DoctorRepository doctorRepository;
    @Mock
    private DoctorsOfficeRepository doctorsOfficeRepository;

    @InjectMocks
    private ShiftService shiftService;

    @Test
    void addShiftTest() {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusHours(4);
        CreateShiftDTO dto = new CreateShiftDTO(1, 10, start, end);

        Doctor doctor = new Doctor("Jan", "Kowalski", "123", Specialization.KARDIOLOGIA, "Adres");
        doctor.setId(1);
        DoctorsOffice office = new DoctorsOffice("101", "RTG");
        office.setId(10);

        when(doctorRepository.findById(1)).thenReturn(Optional.of(doctor));
        when(doctorsOfficeRepository.findById(10)).thenReturn(Optional.of(office));
        when(shiftRepository.doctorBusy(1, start, end)).thenReturn(false);
        when(shiftRepository.doctorsOfficeBusy(10, start, end)).thenReturn(false);

        Shift savedShift = new Shift(doctor, office, start, end);
        savedShift.setId(100);
        when(shiftRepository.save(any(Shift.class))).thenReturn(savedShift);

        ShiftDTO result = shiftService.addShift(dto);

        assertNotNull(result);
        assertEquals(100, result.id());
        assertEquals(1, result.doctorId());
        assertEquals(10, result.officeId());
        verify(shiftRepository).save(any(Shift.class));
    }

    @Test
    void addShiftTimeInvalidTest() {
        CreateShiftDTO dto = new CreateShiftDTO(1, 10, LocalDateTime.now().plusHours(5), LocalDateTime.now().plusHours(1));

        assertThrows(IllegalArgumentException.class, () -> shiftService.addShift(dto));
    }

    @Test
    void addShiftDoctorBusyTest() {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusHours(2);
        CreateShiftDTO dto = new CreateShiftDTO(1, 10, start, end);

        Doctor doctor = new Doctor();
        DoctorsOffice office = new DoctorsOffice();

        when(doctorRepository.findById(1)).thenReturn(Optional.of(doctor));
        when(doctorsOfficeRepository.findById(10)).thenReturn(Optional.of(office));
        when(shiftRepository.doctorBusy(1, start, end)).thenReturn(true);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> shiftService.addShift(dto));
        assertEquals("Doctor is busy", exception.getMessage());
    }

    @Test
    void addShiftOfficeBusyTest() {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusHours(2);
        CreateShiftDTO dto = new CreateShiftDTO(1, 10, start, end);

        Doctor doctor = new Doctor();
        DoctorsOffice office = new DoctorsOffice();

        when(doctorRepository.findById(1)).thenReturn(Optional.of(doctor));
        when(doctorsOfficeRepository.findById(10)).thenReturn(Optional.of(office));
        when(shiftRepository.doctorBusy(1, start, end)).thenReturn(false);
        when(shiftRepository.doctorsOfficeBusy(10, start, end)).thenReturn(true);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> shiftService.addShift(dto));
        assertEquals("DoctorsOffice is busy", exception.getMessage());
    }

    @Test
    void addShiftDoctorNotFoundTest() {
        CreateShiftDTO dto = new CreateShiftDTO(99, 10, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));
        when(doctorRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(DoctorNotFoundException.class, () -> shiftService.addShift(dto));
    }
}