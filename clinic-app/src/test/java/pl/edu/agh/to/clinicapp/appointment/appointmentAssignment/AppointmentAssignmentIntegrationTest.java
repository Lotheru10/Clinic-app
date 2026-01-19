package pl.edu.agh.to.clinicapp.appointment.appointmentAssignment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.to.clinicapp.appointment.Appointment;
import pl.edu.agh.to.clinicapp.appointment.AppointmentRepository;
import pl.edu.agh.to.clinicapp.doctor.Doctor;
import pl.edu.agh.to.clinicapp.doctor.DoctorRepository;
import pl.edu.agh.to.clinicapp.doctor.Specialization;
import pl.edu.agh.to.clinicapp.doctors_office.DoctorsOffice;
import pl.edu.agh.to.clinicapp.doctors_office.DoctorsOfficeRepository;
import pl.edu.agh.to.clinicapp.patient.Patient;
import pl.edu.agh.to.clinicapp.patient.PatientRepository;
import pl.edu.agh.to.clinicapp.shift.Shift;
import pl.edu.agh.to.clinicapp.shift.ShiftRepository;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class AppointmentAssignmentIntegrationTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private DoctorRepository doctorRepository;
    @Autowired private DoctorsOfficeRepository officeRepository;
    @Autowired private ShiftRepository shiftRepository;
    @Autowired private AppointmentRepository appointmentRepository;
    @Autowired private PatientRepository patientRepository;

    private Doctor savedDoctor;
    private DoctorsOffice savedOffice;
    private Patient savedPatient;

    @BeforeEach
    void setUp() {
        savedDoctor = doctorRepository.save(new Doctor("Jan", "Kowalski", "11122233344", Specialization.CHIRURGIA_NACZYNIOWA, "pw"));
        savedOffice = officeRepository.save(new DoctorsOffice("101", "Chirurgia naczyniowa"));
        savedPatient = patientRepository.save(new Patient("Anna", "Nowak", "99988877766", "pw"));
    }

    @Test
    void shouldReturnAllSlotsWhenShiftIsEmpty() throws Exception {
        // given
        LocalDateTime start = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime end = start.plusHours(1);
        shiftRepository.save(new Shift(savedDoctor, savedOffice, start, end));

        // when & then
        mockMvc.perform(get("/api/appointment-assigment/availability")
                        .param("doctorId", String.valueOf(savedDoctor.getId()))
                        .param("start", start.toString())
                        .param("end", end.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                // Używamy startsWith, aby zignorować różnicę "10:00" vs "10:00:00"
                .andExpect(jsonPath("$[0].start", startsWith(start.toString())))
                .andExpect(jsonPath("$[3].start", startsWith(start.plusMinutes(45).toString())));
    }

    @Test
    void shouldExcludeTakenSlots() throws Exception {
        // given
        LocalDateTime start = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime end = start.plusHours(1);
        Shift shift = shiftRepository.save(new Shift(savedDoctor, savedOffice, start, end));

        // Appointment: 10:15 - 10:30
        appointmentRepository.save(new Appointment(shift, start.plusMinutes(15), start.plusMinutes(30), savedPatient));

        // when & then
        mockMvc.perform(get("/api/appointment-assigment/availability")
                        .param("doctorId", String.valueOf(savedDoctor.getId()))
                        .param("start", start.toString())
                        .param("end", end.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].start", startsWith(start.toString())))
                .andExpect(jsonPath("$[1].start", startsWith(start.plusMinutes(30).toString())));
    }

    @Test
    void shouldHandleRequestTimeNarrowerThanShift() throws Exception {
        // given
        LocalDateTime shiftStart = LocalDateTime.now().plusDays(1).withHour(8).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime shiftEnd = shiftStart.plusHours(8);
        shiftRepository.save(new Shift(savedDoctor, savedOffice, shiftStart, shiftEnd));

        LocalDateTime reqStart = shiftStart.plusHours(2); // 10:00
        LocalDateTime reqEnd = reqStart.plusMinutes(30);  // 10:30

        // when & then
        mockMvc.perform(get("/api/appointment-assigment/availability")
                        .param("doctorId", String.valueOf(savedDoctor.getId()))
                        .param("start", reqStart.toString())
                        .param("end", reqEnd.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].start", startsWith(reqStart.toString())))
                .andExpect(jsonPath("$[1].start", startsWith(reqStart.plusMinutes(15).toString())));
    }

    @Test
    void shouldReturnEmptyListWhenNoShifts() throws Exception {
        // given
        LocalDateTime start = LocalDateTime.now().plusDays(2).withHour(12).withMinute(0);
        LocalDateTime end = start.plusHours(1);

        // when & then
        mockMvc.perform(get("/api/appointment-assigment/availability")
                        .param("doctorId", String.valueOf(savedDoctor.getId()))
                        .param("start", start.toString())
                        .param("end", end.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void shouldFailWhenStartDateIsInPast() throws Exception {
        // given
        LocalDateTime pastDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);

        // when & then
        mockMvc.perform(get("/api/appointment-assigment/availability")
                        .param("doctorId", String.valueOf(savedDoctor.getId()))
                        .param("start", pastDate.toString())
                        .param("end", endDate.toString()))
                .andExpect(status().is4xxClientError());
    }
}