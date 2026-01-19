package pl.edu.agh.to.clinicapp.patient;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.agh.to.clinicapp.dto.patient_dto.CreatePatientDTO;
import pl.edu.agh.to.clinicapp.dto.patient_dto.PatientDTO;
import pl.edu.agh.to.clinicapp.dto.patient_dto.PatientDetailsDTO;
import pl.edu.agh.to.clinicapp.exception.patient_exceptions.PatientNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    @Test
    public void getPatients() {
        Patient p1 = new Patient();
        Patient p2 = new Patient();

        p1.setFirstName("Lindsey");
        p1.setLastName("Buckingham");
        p2.setFirstName("Stevie");
        p2.setLastName("Nicks");
        when(patientRepository.findAll()).thenReturn(List.of(p1, p2));

        List<PatientDTO> res = patientService.getPatients();

        assertEquals(2, res.size());
        assertEquals("Lindsey", res.get(0).firstName());
        assertEquals("Buckingham", res.get(0).lastName());

        verify(patientRepository, times(1)).findAll();
        verifyNoMoreInteractions(patientRepository);


    }

    @Test
    public void getPatientById() {
        int id = 1;
        Patient patient = new Patient();
        patient.setId(id);
        patient.setFirstName("Christine");
        patient.setLastName("McVie");
        patient.setAddress("adres1");

        when(patientRepository.findById(id)).thenReturn(Optional.of(patient));

        PatientDetailsDTO res = patientService.getPatientById(id);

        assertEquals("Christine", res.firstName());
        assertEquals("McVie", res.lastName());

        verify(patientRepository, times(1)).findById(id);
        verifyNoMoreInteractions(patientRepository);
    }

    @Test
    void getPatientByIdNotFound() {
        int id = 404;
        when(patientRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(
                PatientNotFoundException.class,
                () -> patientService.getPatientById(id)
        );

        verify(patientRepository, times(1)).findById(id);
        verifyNoMoreInteractions(patientRepository);
    }

    @Test
    void addPatientCallsSave() {
        CreatePatientDTO dto = new CreatePatientDTO(
                "Mick",
                "Fleetwood",
                "12345678910",
                "adres1"
        );

        Patient saved = new Patient("Mick", "Fleetwood", "12345678910", "adres1");
        saved.setId(1);

        when(patientRepository.save(any(Patient.class))).thenReturn(saved);

        PatientDetailsDTO res = patientService.addPatient(dto);

        assertEquals(saved.getId(), res.id());
        assertEquals(saved.getFirstName(), res.firstName());
        assertEquals(saved.getLastName(), res.lastName());
        assertEquals(saved.getAddress(), res.address());

        verify(patientRepository, times(1)).save(any(Patient.class));
        verifyNoMoreInteractions(patientRepository);
    }

    @Test
    void deletePatientCallsDelete() {
        int id = 1;
        patientService.deletePatient(id);

        verify(patientRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(patientRepository);
    }
}
