package pl.edu.agh.to.clinicapp.doctor;


import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.to.clinicapp.dto.CreateDoctorDTO;
import pl.edu.agh.to.clinicapp.dto.DoctorDetailsDTO;


import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
public class DoctorIntegrationTests {

    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private DoctorService doctorService;

    @Autowired
    MockMvc mockMvc;


    @Test
    void addDoctorTest(){
        int before = doctorService.getDoctors().size();
        CreateDoctorDTO doctor = new CreateDoctorDTO("Janusz","Tracz","12345678999",Specialization.KARDIOLOGIA,"abc");
        doctorService.addDoctor(doctor);
        int after = doctorService.getDoctors().size();
        assertEquals("Doctor not added",before + 1, after);
    }

    @ParameterizedTest
    @CsvSource(nullValues = "NULL", value = {
            "NULL,'Tracz','12345678999','KARDIOLOGIA','abc'",
            "'Janusz', NULL,'12345678999','KARDIOLOGIA','abc'",
            "'Janusz', 'Tracz', NULL,'KARDIOLOGIA', 'abc'",
            "'Janusz', 'Tracz', '12345678999', NULL ,'abc'",
            "'Janusz', 'Tracz', '12345678999','KARDIOLOGIA',NULL"
    })
    void addDoctorWithMissingFieldTest(String firstName, String lastName, String pesel, Specialization specialization, String address){
        CreateDoctorDTO doctor = new CreateDoctorDTO(firstName, lastName, pesel, specialization, address);
        assertThrows(ConstraintViolationException.class,
                () -> doctorService.addDoctor(doctor));
    }

    @Test
    void addDoctorWithBadPeselNumberTest(){
        CreateDoctorDTO doctor = new CreateDoctorDTO(" ","Tracz","123",Specialization.KARDIOLOGIA,"abc");
        assertThrows(ConstraintViolationException.class,
                () -> doctorService.addDoctor(doctor));
    }

    @Test
    void deleteDoctorTest(){
        CreateDoctorDTO doctor = new CreateDoctorDTO("Janusz","Tracz","12345678999",Specialization.KARDIOLOGIA,"abc");
        DoctorDetailsDTO savedDoctor = doctorService.addDoctor(doctor);
        int id = savedDoctor.id();
        int before = doctorService.getDoctors().size();
        doctorService.deleteDoctor(id);
        int after = doctorService.getDoctors().size();
        assertEquals("Doctor not deleted",before - 1, after);
    }

    @Test
    void doctorDetailsTest(){
        CreateDoctorDTO doctor = new CreateDoctorDTO("Janusz","Tracz","12345678999",Specialization.KARDIOLOGIA,"abc");
        DoctorDetailsDTO savedDoctor = doctorService.addDoctor(doctor);
        String firstName = savedDoctor.firstName();
        String lastName = savedDoctor.lastName();
        Specialization specialization = savedDoctor.specialization();
        String address = savedDoctor.address();
        assertEquals("Bad first name", "Janusz", firstName );
        assertEquals("Bad last name", "Tracz", lastName );
        assertEquals("Bad specialization", Specialization.KARDIOLOGIA, specialization );
        assertEquals("Bad address", "abc", address );
    }

    @Test
    void addTwoDoctorsTest(){
        int before = doctorService.getDoctors().size();
        CreateDoctorDTO doctor = new CreateDoctorDTO("Janusz","Tracz","12345678999",Specialization.KARDIOLOGIA,"abc");
        CreateDoctorDTO doctor2 = new CreateDoctorDTO("Marek","Tracz","12345678991",Specialization.KARDIOLOGIA,"abc");

        doctorService.addDoctor(doctor);
        doctorService.addDoctor(doctor2);

        int after = doctorService.getDoctors().size();
        assertEquals("Doctor not added",before + 2, after);
    }

    @Test
    void addTwoDoctorsWithTheSamePeselNumberTest() {
        CreateDoctorDTO doctor = new CreateDoctorDTO("Janusz", "Tracz", "12345678999", Specialization.KARDIOLOGIA, "abc");
        CreateDoctorDTO doctor2 = new CreateDoctorDTO("Marek", "Tracz", "12345678999", Specialization.KARDIOLOGIA, "abc");

        doctorService.addDoctor(doctor);
        assertThrows(DataIntegrityViolationException.class,
                () -> {
                    doctorService.addDoctor(doctor2);
                    doctorRepository.flush(); //to catch exception during transaction
                });
    }



    //Endpoints tests

    @Test
    void returnDoctorById() throws Exception {
        CreateDoctorDTO doctor = new CreateDoctorDTO("Janusz", "Tracz", "12345678999", Specialization.KARDIOLOGIA, "abc");
        DoctorDetailsDTO savedDoctor = doctorService.addDoctor(doctor);
        int id = savedDoctor.id();

        mockMvc.perform(get("/api/doctors/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").exists())
                .andExpect(jsonPath("$.lastName").exists())
                .andExpect(jsonPath("$.specialization").exists())
                .andExpect(jsonPath("$.address").exists());

    }
    @Test
    void returnDoctorsList() throws Exception {
        CreateDoctorDTO doctor = new CreateDoctorDTO("Janusz","Tracz","12345678999",Specialization.KARDIOLOGIA,"abc");
        CreateDoctorDTO doctor2 = new CreateDoctorDTO("Marek","Tracz","12345678991",Specialization.KARDIOLOGIA,"abc");

        doctorService.addDoctor(doctor);
        doctorService.addDoctor(doctor2);

        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").exists())
                .andExpect(jsonPath("$[1].firstName").exists());
    }

    @Test
    void returnDeleteDoctor() throws Exception {
        CreateDoctorDTO doctor = new CreateDoctorDTO("Janusz", "Tracz", "12345678999", Specialization.KARDIOLOGIA, "abc");
        DoctorDetailsDTO savedDoctor = doctorService.addDoctor(doctor);
        int id = savedDoctor.id();

        mockMvc.perform(delete("/api/doctors/{id}", id))
                .andExpect(status().isNoContent());

    }

}
