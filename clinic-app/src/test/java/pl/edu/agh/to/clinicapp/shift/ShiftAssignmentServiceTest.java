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
import pl.edu.agh.to.clinicapp.dto.shift_dto.AvailabilityDTO;
import pl.edu.agh.to.clinicapp.shift.shift_assignment.ShiftAssignmentService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShiftAssignmentServiceTest {

    @Mock
    private ShiftRepository shiftRepository;
    @Mock
    private DoctorRepository doctorRepository;
    @Mock
    private DoctorsOfficeRepository doctorsOfficeRepository;

    @InjectMocks
    private ShiftAssignmentService shiftAssignmentService;

    @Test
    void checkAvailabilityFiltersOutBusyResources() {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusHours(2);

        Doctor freeDoc = new Doctor("Free", "Doc", "1", Specialization.KARDIOLOGIA, "A");
        freeDoc.setId(1);
        Doctor busyDoc = new Doctor("Busy", "Doc", "2", Specialization.CHIRURGIA_OGOLNA, "B");
        busyDoc.setId(2);

        DoctorsOffice freeOffice = new DoctorsOffice("100", "Free");
        freeOffice.setId(10);
        DoctorsOffice busyOffice = new DoctorsOffice("200", "Busy");
        busyOffice.setId(20);

        Shift overlappingShift = new Shift(busyDoc, busyOffice, start, end);

        when(shiftRepository.overlappingShifts(start, end)).thenReturn(List.of(overlappingShift));
        when(doctorRepository.findAll()).thenReturn(List.of(freeDoc, busyDoc));
        when(doctorsOfficeRepository.findAll()).thenReturn(List.of(freeOffice, busyOffice));

        AvailabilityDTO result = shiftAssignmentService.checkAvailability(start, end);

        assertEquals(1, result.freeDoctors().size());
        assertTrue(result.freeDoctors().stream().anyMatch(d -> d.id() == 1));
        assertFalse(result.freeDoctors().stream().anyMatch(d -> d.id() == 2));

        assertEquals(1, result.freeDoctorsOffices().size());
        assertTrue(result.freeDoctorsOffices().stream().anyMatch(o -> o.id() == 10));
        assertFalse(result.freeDoctorsOffices().stream().anyMatch(o -> o.id() == 20));
    }

    @Test
    void checkAvailabilityReturnsAllWhenNoShiftsOverlap() {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusHours(2);

        Doctor doc = new Doctor();
        doc.setId(1);
        DoctorsOffice office = new DoctorsOffice();
        office.setId(10);

        when(shiftRepository.overlappingShifts(start, end)).thenReturn(Collections.emptyList());
        when(doctorRepository.findAll()).thenReturn(List.of(doc));
        when(doctorsOfficeRepository.findAll()).thenReturn(List.of(office));

        AvailabilityDTO result = shiftAssignmentService.checkAvailability(start, end);

        assertEquals(1, result.freeDoctors().size());
        assertEquals(1, result.freeDoctorsOffices().size());
    }
}