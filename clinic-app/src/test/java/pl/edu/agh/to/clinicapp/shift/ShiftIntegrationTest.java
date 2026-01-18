package pl.edu.agh.to.clinicapp.shift;

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
import pl.edu.agh.to.clinicapp.doctor.DoctorService;
import pl.edu.agh.to.clinicapp.doctor.Specialization;
import pl.edu.agh.to.clinicapp.doctors_office.DoctorsOffice;
import pl.edu.agh.to.clinicapp.doctors_office.DoctorsOfficeRepository;
import pl.edu.agh.to.clinicapp.doctors_office.DoctorsOfficeService;
import pl.edu.agh.to.clinicapp.dto.shift_dto.CreateShiftDTO;
import pl.edu.agh.to.clinicapp.exception.doctor_exceptions.DoctorHasShiftException;
import pl.edu.agh.to.clinicapp.exception.doctor_office_exceptions.DoctorsOfficeHasShiftException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class ShiftIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private DoctorService doctorService;
    @Autowired private DoctorRepository doctorRepository;
    @Autowired private DoctorsOfficeService officeService;
    @Autowired private DoctorsOfficeRepository officeRepository;
    @Autowired private ShiftService shiftService;
    @Autowired private ShiftRepository shiftRepository;
    @Autowired private ObjectMapper objectMapper;

    private Doctor savedDoctor;
    private DoctorsOffice savedOffice;

    @BeforeEach
    void setUp() {
        savedDoctor = doctorRepository.save(new Doctor("Jan", "Testowy", "12345678901", Specialization.KARDIOLOGIA, "Adres"));
        savedOffice = officeRepository.save(new DoctorsOffice("101", "RTG"));
    }

    @Test
    void shouldCreateShiftSuccessfully() throws Exception {
        LocalDateTime start = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0);
        LocalDateTime end = start.plusHours(2);

        CreateShiftDTO dto = new CreateShiftDTO(savedDoctor.getId(), savedOffice.getId(), start, end);

        mockMvc.perform(post("/api/shifts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        assertTrue(shiftRepository.doctorBusy(savedDoctor.getId(), start.plusMinutes(1), end.minusMinutes(1)));
    }

    @Test
    void shouldFailWhenDoctorIsBusy() throws Exception {
        LocalDateTime start = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0);
        LocalDateTime end = start.plusHours(2);

        shiftRepository.save(new Shift(savedDoctor, savedOffice, start, end));

        CreateShiftDTO overlapDto = new CreateShiftDTO(savedDoctor.getId(), savedOffice.getId(), start.plusHours(1), end.plusHours(1));

        Exception exception = assertThrows(Exception.class, () -> {
            mockMvc.perform(post("/api/shifts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(overlapDto)));
        });

        Throwable rootCause = exception;
        if (exception instanceof jakarta.servlet.ServletException && exception.getCause() != null) {
            rootCause = exception.getCause();
        }
        
        assertInstanceOf(IllegalStateException.class, rootCause, "Oczekiwano IllegalStateException, a otrzymano: " + rootCause.getClass().getName());
        assertEquals("Doctor is busy", rootCause.getMessage());
    }

    @Test
    void cantDeleteDoctorAndOfficeWhileHaveShiftsTest() {
        LocalDateTime start = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0);
        LocalDateTime end = start.plusHours(2);

        CreateShiftDTO shiftDTO = new CreateShiftDTO(savedDoctor.getId(), savedOffice.getId(), start, end);
        shiftService.addShift(shiftDTO);

        assertThrows(DoctorHasShiftException.class, () -> {
            doctorService.deleteDoctor(savedDoctor.getId());
        });
        assertThrows(DoctorsOfficeHasShiftException.class, () -> {
            officeService.deleteDoctorsOffice(savedOffice.getId());
        });
    }

}