package pl.edu.agh.to.clinicapp.doctor;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.agh.to.clinicapp.dto.DoctorDTO;
import pl.edu.agh.to.clinicapp.dto.DoctorDetailsDTO;
import pl.edu.agh.to.clinicapp.exception.DoctorNotFoundException;

import javax.print.Doc;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DoctorServiceTest {

    @Mock
    DoctorRepository doctorRepository;

    @InjectMocks
    DoctorService doctorService;


    @Test
    void getDoctors() {
        Doctor doctor = new Doctor("Janusz", "Tracz", "12345678999", "cardiologist", "abc");
        Doctor doctor2 = new Doctor("Marek", "Tracz", "12345678991", "cardiologist", "abc");
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
        Doctor doctor = new Doctor("Janusz", "Tracz", "12345678999", "cardiologist", "abc");
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
        Doctor doctor = new Doctor("Janusz", "Tracz", "12345678999", "cardiologist", "abc");
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        Doctor saved = doctorService.addDoctor(doctor);

        assertEquals(saved, doctor);
        verify(doctorRepository, times(1)).save(doctor);
        verifyNoMoreInteractions(doctorRepository);
    }

    @Test
    void deleteDoctorCallsDelete(){
        int id = 1;
        doctorService.deleteDoctor(id);

        verify(doctorRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(doctorRepository);

    }
}
