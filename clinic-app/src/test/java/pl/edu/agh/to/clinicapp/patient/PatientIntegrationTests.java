package pl.edu.agh.to.clinicapp.patient;


import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.to.clinicapp.dto.patient_dto.CreatePatientDTO;
import pl.edu.agh.to.clinicapp.dto.patient_dto.PatientDetailsDTO;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
public class PatientIntegrationTests {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientService patientService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void addPatientTest() {
        int before = patientService.getPatients().size();
        CreatePatientDTO dto = new CreatePatientDTO("Freddie", "Mercury", "01234567890", "adres1");
        patientService.addPatient(dto);
        int after = patientService.getPatients().size();

        assertEquals("Patient not added", before+1, after);
    }

    @ParameterizedTest
    @CsvSource(nullValues = "NULL", value={
            "NULL, 'Jackson', '01020304050', 'heehee'",
            "'Michael', NULL, '01020304050', 'heehee'",
            "'Michael', 'Jackson', NULL, 'heehee'",
            "'Michael', 'Jackson', '01020304050', NULL"
    })
    void addPatientWithMissingFieldTest(String fstName, String lastName, String pesel, String adress) {
        CreatePatientDTO dto = new CreatePatientDTO(fstName, lastName, pesel, adress);

        assertThrows(ConstraintViolationException.class,
                () -> patientService.addPatient(dto));
    }

    @Test
    void addPatientWithBadPeselTest() {
        CreatePatientDTO dto = new CreatePatientDTO("Dolly", "Parton", "123", "adres1");
        assertThrows(ConstraintViolationException.class,
                () -> patientService.addPatient(dto));
    }

    @Test
    void deletePatientTest() {
        CreatePatientDTO dto = new CreatePatientDTO("David", "Bowie", "12345678910", "adres1");
        PatientDetailsDTO saved = patientService.addPatient(dto);
        int id = saved.id();

        int before = patientService.getPatients().size();
        patientService.deletePatient(id);
        int after = patientService.getPatients().size();

        assertEquals("Patient not deleted", before-1, after);
    }

    @Test
    void patientDetailsTest() {
        CreatePatientDTO dto = new CreatePatientDTO("Don", "McLean", "12345678910", "adres1");
        PatientDetailsDTO saved = patientService.addPatient(dto);

        String firstName = saved.firstName();
        String lastName = saved.lastName();
        String address = saved.address();

        assertEquals("Bad first name", "Don", firstName);
        assertEquals("Bad last name", "McLean", lastName);
        assertEquals("Bad address", "adres1", address);
    }

    @Test
    void addTwoPatientsTest() {
        int before = patientService.getPatients().size();
        CreatePatientDTO p1 = new CreatePatientDTO("Elton", "John", "12345678910", "adres1");
        CreatePatientDTO p2 = new CreatePatientDTO("Jim", "Croce", "22345678910", "adres1");

        patientService.addPatient(p1);
        patientService.addPatient(p2);

        int after = patientService.getPatients().size();
        assertEquals("Patients not added", before+2, after);
    }

    @Test
    void addTwoPatientsWithSamePeselTest() {
        String pesel = "12345678910";
        CreatePatientDTO p1 = new CreatePatientDTO("Marvin", "Gaye", pesel, "adres1");
        CreatePatientDTO p2 = new CreatePatientDTO("Tammi", "Terrell", pesel, "adres1");

        patientService.addPatient(p1);

        assertThrows(DataIntegrityViolationException.class,
                () -> {
                    patientService.addPatient(p2);
                    patientRepository.flush();
                });
    }

    // ENDPOINTS
    @Test
    void returnPatientById() throws Exception {
        CreatePatientDTO dto = new CreatePatientDTO("Kendrick", "Lamar", "12345678910", "adres1");
        PatientDetailsDTO saved = patientService.addPatient(dto);
        int id = saved.id();

        mockMvc.perform(get("/api/patients/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").exists())
                .andExpect(jsonPath("$.lastName").exists())
                .andExpect(jsonPath("$.address").exists());
    }

    @Test
    void returnPatientsList() throws Exception {
        CreatePatientDTO p1 = new CreatePatientDTO("Two", "Pac",  "12345678910", "adres1");
        CreatePatientDTO p2 = new CreatePatientDTO("Notorious", "BIG", "22345678910", "adres1");

        patientService.addPatient(p1);
        patientService.addPatient(p2);

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").exists()).andExpect(jsonPath("$[1].firstName").exists())
                .andExpect(jsonPath("$[1].firstName").exists());
    }

    @Test
    void returnDeletePatient() throws Exception {
        CreatePatientDTO patient = new CreatePatientDTO("Marshall", "Mathers", "12345678910", "adres1");
        PatientDetailsDTO saved = patientService.addPatient(patient);
        int id = saved.id();

        mockMvc.perform(delete("/api/patients/{id}", id))
                .andExpect(status().isNoContent());
    }
}
