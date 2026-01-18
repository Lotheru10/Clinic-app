package pl.edu.agh.to.clinicapp.appointment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.to.clinicapp.doctor.Doctor;
import pl.edu.agh.to.clinicapp.doctor.DoctorRepository;
import pl.edu.agh.to.clinicapp.doctor.Specialization;
import pl.edu.agh.to.clinicapp.doctors_office.DoctorsOffice;
import pl.edu.agh.to.clinicapp.doctors_office.DoctorsOfficeRepository;
import pl.edu.agh.to.clinicapp.dto.appointment_dto.CreateAppointmentDTO;
import pl.edu.agh.to.clinicapp.patient.Patient;
import pl.edu.agh.to.clinicapp.patient.PatientRepository;
import pl.edu.agh.to.clinicapp.shift.Shift;
import pl.edu.agh.to.clinicapp.shift.ShiftRepository;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class AppointmentIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Autowired private DoctorRepository doctorRepository;
    @Autowired private DoctorsOfficeRepository officeRepository;
    @Autowired private ShiftRepository shiftRepository;
    @Autowired private PatientRepository patientRepository;
    @Autowired private AppointmentRepository appointmentRepository;

    private Patient savedPatient;
    private Shift savedShift;

    @BeforeEach
    void setUp() {

        Doctor doctor = doctorRepository.save(new Doctor("Adam", "Nowak", "11122233344", Specialization.INTENSYWNA_TERAPIA, "abcd"));
        DoctorsOffice office = officeRepository.save(new DoctorsOffice("202", "Gabinet"));
        savedPatient = patientRepository.save(new Patient("Ewa", "Kowalska", "99988877766", "acdb"));

        LocalDateTime shiftStart = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime shiftEnd = shiftStart.plusHours(2);
        savedShift = shiftRepository.save(new Shift(doctor, office, shiftStart, shiftEnd));
    }

    @Test
    void shouldCreateAppointmentSuccessfully() throws Exception {
        // given
        LocalDateTime appointmentStart = savedShift.getStart().plusMinutes(15); // 10:15
        CreateAppointmentDTO dto = new CreateAppointmentDTO(savedPatient.getId(), savedShift.getId(), appointmentStart);

        // when & then
        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.doctorName").value("Adam Nowak"));

        // Verify database
        boolean isBusy = appointmentRepository.shiftBusy(savedShift.getId(), appointmentStart, appointmentStart.plusMinutes(15));
        assertTrue(isBusy, "Slot should be marked as busy in DB");
    }

    @Test
    void shouldFailWhenSlotIsTaken() throws Exception {
        // given
        LocalDateTime start = savedShift.getStart();

        Appointment existing = new Appointment(savedShift, start, start.plusMinutes(15), savedPatient);
        appointmentRepository.save(existing);

        Patient anotherPatient = patientRepository.save(
                new Patient("Marek", "Nowak", "12312312312", "abc")
        );

        CreateAppointmentDTO dto = new CreateAppointmentDTO(anotherPatient.getId(), savedShift.getId(), start);

        // when & then
        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", containsString("Slot already taken")));
    }

    @Test
    void shouldFailWhenPatientHasAnotherAppointment() throws Exception {
        // given
        LocalDateTime start = savedShift.getStart();

        Appointment existing = new Appointment(savedShift, start, start.plusMinutes(15), savedPatient);
        appointmentRepository.save(existing);

        Doctor doc2 = doctorRepository.save(new Doctor("Jan", "Kowalski", "22233344455", Specialization.OKULISTYKA, "abc"));
        Shift otherShift = shiftRepository.save(new Shift(doc2, savedShift.getOffice(), start, start.plusHours(2)));

        CreateAppointmentDTO dto = new CreateAppointmentDTO(savedPatient.getId(), otherShift.getId(), start);

        // when & then
        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", containsString("has appointment at given time")));
    }
}