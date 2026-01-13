package pl.edu.agh.to.clinicapp.shift;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.to.clinicapp.doctor.Doctor;
import pl.edu.agh.to.clinicapp.doctor.DoctorRepository;
import pl.edu.agh.to.clinicapp.doctor.Specialization;
import pl.edu.agh.to.clinicapp.doctors_office.DoctorsOffice;
import pl.edu.agh.to.clinicapp.doctors_office.DoctorsOfficeRepository;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class ShiftAssignmentIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private DoctorRepository doctorRepository;
    @Autowired private DoctorsOfficeRepository officeRepository;
    @Autowired private ShiftRepository shiftRepository;

    @BeforeEach
    void setUp() {
        DoctorsOffice office1 = officeRepository.save(new DoctorsOffice("101", "A"));
        DoctorsOffice office2 = officeRepository.save(new DoctorsOffice("102", "B"));

        Doctor doc1 = doctorRepository.save(new Doctor("Jan", "Zajety", "111", Specialization.KARDIOLOGIA, "A"));
        Doctor doc2 = doctorRepository.save(new Doctor("Anna", "Wolna", "222", Specialization.CHIRURGIA_OGOLNA, "B"));

        LocalDateTime start = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0);
        LocalDateTime end = start.plusHours(2);
        shiftRepository.save(new Shift(doc1, office1, start, end));
    }

    @Test
    void shouldReturnOnlyFreeDoctorsAndOffices() throws Exception {
        LocalDateTime queryStart = LocalDateTime.now().plusDays(1).withHour(10).withMinute(30);
        LocalDateTime queryEnd = queryStart.plusHours(1);

        mockMvc.perform(get("/api/shift-assignment/avaibility")
                        .param("start", queryStart.toString())
                        .param("end", queryEnd.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.freeDoctors", hasSize(1)))
                .andExpect(jsonPath("$.freeDoctors[0].lastName").value("Wolna"))
                .andExpect(jsonPath("$.freeDoctorsOffices", hasSize(1)))
                .andExpect(jsonPath("$.freeDoctorsOffices[0].roomNumber").value("102"));
    }
}