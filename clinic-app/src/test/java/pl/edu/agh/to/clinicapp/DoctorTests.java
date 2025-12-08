package pl.edu.agh.to.clinicapp;


import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import pl.edu.agh.to.clinicapp.doctor.Doctor;
import pl.edu.agh.to.clinicapp.doctor.DoctorRepository;
import pl.edu.agh.to.clinicapp.doctor.DoctorService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@ActiveProfiles("test")
@Transactional //to clean up after each test

public class DoctorTests {

    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private DoctorService doctorService;



    @Test
    void addDoctorTest(){
        int before = doctorService.getDoctors().size();
        Doctor doctor = new Doctor("Janusz","Tracz","12345678999","cardiologist","abc");
        doctorService.addDoctor(doctor);
        int after = doctorService.getDoctors().size();
        assertEquals("Doctor not added",before + 1, after);
    }

    @ParameterizedTest
    @CsvSource({
            "' ','Tracz','12345678999','cardiologist','abc'",
            "'Janusz', ' ','12345678999','cardiologist','abc'",
            "'Janusz', 'Tracz', '','cardiologist', 'abc'",
            "'Janusz', 'Tracz', '12345678999', '' ,'abc'",
            "'Janusz', 'Tracz', '12345678999','cardiologist',' '"
    })
    void addDoctorWithMissingFieldTest(String firstName, String lastName, String pesel, String specialization, String address){
        Doctor doctor = new Doctor(firstName, lastName, pesel, specialization, address);
        assertThrows(ConstraintViolationException.class,
                () -> doctorService.addDoctor(doctor));
    }

    @Test
    void addDoctorWithBadPeselNumberTest(){
        Doctor doctor = new Doctor(" ","Tracz","123","cardiologist","abc");
        assertThrows(ConstraintViolationException.class,
                () -> doctorService.addDoctor(doctor));
    }

    @Test
    void deleteDoctorTest(){
        Doctor doctor = new Doctor("Janusz","Tracz","12345678999","cardiologist","abc");
        Doctor savedDoctor = doctorService.addDoctor(doctor);
        int id = savedDoctor.getId();
        int before = doctorService.getDoctors().size();
        doctorService.deleteDoctor(savedDoctor);
        int after = doctorService.getDoctors().size();
        assertEquals("Doctor not deleted",before - 1, after);
    }

    @Test
    void doctorDetailsTest(){
        Doctor doctor = new Doctor("Janusz","Tracz","12345678999","cardiologist","abc");
        Doctor savedDoctor = doctorService.addDoctor(doctor);
        String firstName = savedDoctor.getFirstName();
        String lastName = savedDoctor.getLastName();
        String pesel = savedDoctor.getPeselNumber();
        String specialization = savedDoctor.getSpecialization();
        String address = savedDoctor.getAddress();
        assertEquals("Bad first name", "Janusz", firstName );
        assertEquals("Bad last name", "Tracz", lastName );
        assertEquals("Bad pesel", "12345678999",pesel );
        assertEquals("Bad specialization", "cardiologist", specialization );
        assertEquals("Bad address", "abc", address );
    }

    @Test
    void addTwoDoctorsTest(){
        int before = doctorService.getDoctors().size();
        Doctor doctor = new Doctor("Janusz","Tracz","12345678999","cardiologist","abc");
        Doctor doctor2 = new Doctor("Marek","Tracz","12345678991","cardiologist","abc");

        doctorService.addDoctor(doctor);
        doctorService.addDoctor(doctor2);

        int after = doctorService.getDoctors().size();
        assertEquals("Doctor not added",before + 2, after);
    }

    @Test
    void addTwoDoctorsWithTheSamePeselNumberTest() {
        Doctor doctor = new Doctor("Janusz", "Tracz", "12345678999", "cardiologist", "abc");
        Doctor doctor2 = new Doctor("Marek", "Tracz", "12345678999", "cardiologist", "abc");

        doctorService.addDoctor(doctor);
        assertThrows(DataIntegrityViolationException.class,
                () -> {
                    doctorService.addDoctor(doctor2);
                    doctorRepository.flush(); //to catch exception during transaction
                });
    }
}
