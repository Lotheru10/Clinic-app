package pl.edu.agh.to.clinicapp.patient;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class PatientRepositoryTests {

    @Autowired
    private PatientRepository patientRepository;

    @Test
    void saveAndFindWorks(){
        Patient patient = new Patient("Billy", "Joel", "12345678910", "adres1");
        Patient saved = patientRepository.save(patient);

        assertTrue(patientRepository.findById(saved.getId()).isPresent());
    }
}
