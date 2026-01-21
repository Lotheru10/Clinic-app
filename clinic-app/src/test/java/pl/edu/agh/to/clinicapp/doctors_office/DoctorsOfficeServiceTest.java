package pl.edu.agh.to.clinicapp.doctors_office;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.agh.to.clinicapp.doctor.Doctor;
import pl.edu.agh.to.clinicapp.doctor.Specialization;
import pl.edu.agh.to.clinicapp.dto.doctors_office_dto.CreateDoctorsOfficeDTO;
import pl.edu.agh.to.clinicapp.dto.doctors_office_dto.DoctorsOfficeDTO;
import pl.edu.agh.to.clinicapp.dto.doctors_office_dto.DoctorsOfficeDetailsDTO;
import pl.edu.agh.to.clinicapp.exception.doctor_office_exceptions.DoctorsOfficeNotFoundException;
import pl.edu.agh.to.clinicapp.shift.Shift;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
public class DoctorsOfficeServiceTest {

    @Mock
    private DoctorsOfficeRepository doctorsOfficeRepository;

    @InjectMocks
    private DoctorsOfficeService doctorsOfficeService;

    @Test
    void getAllDoctorsOffices() {
        DoctorsOffice office1 = new DoctorsOffice("101", "Gabinet Zabiegowy");
        office1.setId(1);
        DoctorsOffice office2 = new DoctorsOffice("102", "Gabinet Lekarski");
        office2.setId(2);

        when(doctorsOfficeRepository.findAll()).thenReturn(List.of(office1, office2));

        List<DoctorsOfficeDTO> result = doctorsOfficeService.getAllDoctorsOffices();

        assertEquals(2, result.size());
        assertEquals("101", result.get(0).roomNumber());
        assertEquals("102", result.get(1).roomNumber());
        verify(doctorsOfficeRepository, times(1)).findAll();
    }

    @Test
    void getDoctorsOfficeByIdReturnDetails() {
        int id = 1;
        DoctorsOffice office = new DoctorsOffice("101", "Opis");
        office.setId(id);

        when(doctorsOfficeRepository.findById(id)).thenReturn(Optional.of(office));

        DoctorsOfficeDetailsDTO result = doctorsOfficeService.getDoctorsOfficeById(id);

        assertEquals(id, result.id());
        assertEquals("101", result.roomNumber());
        assertEquals("Opis", result.roomDescription());
    }

    @Test
    void getDoctorsOfficeByIdNotFound() {
        int id = 99;
        when(doctorsOfficeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(DoctorsOfficeNotFoundException.class,
                () -> doctorsOfficeService.getDoctorsOfficeById(id));
    }

    @Test
    void addDoctorsOfficeSaveAndReturnDto() {
        CreateDoctorsOfficeDTO createDTO = new CreateDoctorsOfficeDTO("202", "Okulista");

        DoctorsOffice savedEntity = new DoctorsOffice("202", "Okulista");
        savedEntity.setId(10);

        when(doctorsOfficeRepository.save(any(DoctorsOffice.class))).thenReturn(savedEntity);

        DoctorsOfficeDetailsDTO result = doctorsOfficeService.addDoctorsOffice(createDTO);

        assertEquals(10, result.id());
        assertEquals("202", result.roomNumber());
        assertEquals("Okulista", result.roomDescription());

        verify(doctorsOfficeRepository).save(any(DoctorsOffice.class));
    }

    @Test
    void deleteDoctorThrowsWhenNotFound() {
        int id = 33;
        when(doctorsOfficeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(DoctorsOfficeNotFoundException.class, () -> doctorsOfficeService.deleteDoctorsOffice(id));

        verify(doctorsOfficeRepository).findById(id);
        verify(doctorsOfficeRepository, never()).deleteById(anyInt());
    }


    @Test
    void getDoctorOfficeByIdMapShiftsCorrectly() {
        int officeId = 10;
        DoctorsOffice office = new DoctorsOffice("202", "Gabinet");
        office.setId(officeId);

        Doctor doctor = new Doctor("Anna", "Nowak", "999", Specialization.OKULISTYKA, "Adres");
        doctor.setId(2);

        Shift shift = new Shift(doctor, office, LocalDateTime.now(), LocalDateTime.now().plusHours(4));
        office.getShifts().add(shift);

        when(doctorsOfficeRepository.findById(officeId)).thenReturn(Optional.of(office));

        DoctorsOfficeDetailsDTO res = doctorsOfficeService.getDoctorsOfficeById(officeId);

        assertEquals(officeId, res.id());
        assertEquals(1, res.shifts().size());

        assertEquals(2, res.shifts().getFirst().doctorId());
        assertTrue(res.shifts().getFirst().doctorName().contains("Anna"));
        assertTrue(res.shifts().getFirst().doctorName().contains("Nowak"));
    }
}
