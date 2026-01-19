package pl.edu.agh.to.clinicapp.doctor;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.agh.to.clinicapp.doctors_office.DoctorsOffice;
import pl.edu.agh.to.clinicapp.dto.doctor_dto.CreateDoctorDTO;
import pl.edu.agh.to.clinicapp.dto.doctor_dto.DoctorDTO;
import pl.edu.agh.to.clinicapp.dto.doctor_dto.DoctorDetailsDTO;
import pl.edu.agh.to.clinicapp.exception.doctor_exceptions.DoctorNotFoundException;
import pl.edu.agh.to.clinicapp.shift.Shift;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DoctorServiceTest {

    @Mock
    DoctorRepository doctorRepository;

    @InjectMocks
    DoctorService doctorService;


    @Test
    void getDoctors() {
        Doctor doctor = new Doctor("Janusz", "Tracz", "12345678999", Specialization.KARDIOLOGIA, "abc");
        Doctor doctor2 = new Doctor("Marek", "Tracz", "12345678991", Specialization.KARDIOLOGIA, "abc");
        when(doctorRepository.findAll()).thenReturn(List.of(doctor, doctor2));

        List<DoctorDTO> result = doctorService.getDoctors();

        assertEquals(2, result.size());
        assertEquals("Janusz", result.get(0).firstName());
        assertEquals("Marek", result.get(1).firstName());

        //checking if findAll was called one time
        verify(doctorRepository, times(1)).findAll();
        verifyNoMoreInteractions(doctorRepository);

    }

    @Test
    void getDoctorById() {
        int id = 1;
        Doctor doctor = new Doctor("Janusz", "Tracz", "12345678999", Specialization.KARDIOLOGIA, "abc");
        when(doctorRepository.findById(id)).thenReturn(Optional.of(doctor));

        DoctorDetailsDTO result = doctorService.getDoctorById(id);

        assertEquals("Janusz", result.firstName());

        verify(doctorRepository, times(1)).findById(id);
        verifyNoMoreInteractions(doctorRepository);

    }

    @Test
    void getDoctorByIdWhenIdNotFound() {
        int id = 1;
        when(doctorRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(
                DoctorNotFoundException.class,
                () -> doctorService.getDoctorById(id)
        );

        verify(doctorRepository, times(1)).findById(id);
        verifyNoMoreInteractions(doctorRepository);

    }

    @Test
    void addDoctorCallsSave(){
        CreateDoctorDTO createDoctorDTO = new CreateDoctorDTO("Janusz", "Tracz", "12345678999", Specialization.KARDIOLOGIA, "abc");

        Doctor savedEntity = new Doctor("Janusz", "Tracz", "12345678999", Specialization.KARDIOLOGIA, "abc");
        savedEntity.setId(1);


        when(doctorRepository.save(any(Doctor.class))).thenReturn(savedEntity);

        DoctorDetailsDTO result = doctorService.addDoctor(createDoctorDTO);

        assertEquals(savedEntity.getId(), result.id());
        assertEquals(savedEntity.getFirstName(), result.firstName());
        assertEquals(savedEntity.getLastName(), result.lastName());
        assertEquals(savedEntity.getAddress(), result.address());
        assertEquals(savedEntity.getSpecialization(), result.specialization());

        verify(doctorRepository, times(1)).save(any(Doctor.class));
        verifyNoMoreInteractions(doctorRepository);
    }

    @Test
    void deleteDoctorThrowsWhenNotFound() {
        int id = 33;
        when(doctorRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(DoctorNotFoundException.class, () -> doctorService.deleteDoctor(id));

        verify(doctorRepository).findById(id);
        verify(doctorRepository, never()).deleteById(anyInt());
    }


    @Test
    void getDoctorByIdMapShiftsCorrectly() {
        int doctorId = 1;
        Doctor doctor = new Doctor("Jan", "Kowalski", "123", Specialization.KARDIOLOGIA, "Adres");
        doctor.setId(doctorId);

        DoctorsOffice office = new DoctorsOffice("101", "RTG");
        office.setId(5);

        Shift shift = new Shift(doctor, office, LocalDateTime.now(), LocalDateTime.now().plusHours(2));
        doctor.getShifts().add(shift);

        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));

        DoctorDetailsDTO res = doctorService.getDoctorById(doctorId);

        assertEquals(doctorId, res.id());
        assertEquals(1, res.shifts().size());

        assertEquals(5, res.shifts().getFirst().officeId());
        assertTrue(res.shifts().getFirst().officeName().contains("101"));
        assertTrue(res.shifts().getFirst().officeName().contains("RTG"));
    }
}
