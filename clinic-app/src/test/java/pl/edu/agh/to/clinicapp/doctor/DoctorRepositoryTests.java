package pl.edu.agh.to.clinicapp.doctor;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class DoctorRepositoryTests {

    @Autowired
    private DoctorRepository doctorRepository;

    @Test
    void saveAndFindWorks(){
        Doctor doctor = new Doctor("Janusz", "Tracz", "12345678999", Specialization.KARDIOLOGIA, "abc");
        Doctor saved = doctorRepository.save(doctor);

        assertTrue(doctorRepository.findById(saved.getId()).isPresent());
    }
}
