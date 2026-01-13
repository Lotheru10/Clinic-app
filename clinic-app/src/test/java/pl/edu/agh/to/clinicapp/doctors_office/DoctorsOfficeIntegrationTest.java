package pl.edu.agh.to.clinicapp.doctors_office;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.to.clinicapp.dto.doctors_office_dto.CreateDoctorsOfficeDTO;
import pl.edu.agh.to.clinicapp.dto.doctors_office_dto.DoctorsOfficeDetailsDTO;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class DoctorsOfficeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DoctorsOfficeRepository doctorsOfficeRepository;

    @Autowired
    private DoctorsOfficeService doctorsOfficeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateAndGetDoctorsOffice() throws Exception {
        CreateDoctorsOfficeDTO newOffice = new CreateDoctorsOfficeDTO("101", "Gabinet RTG");
        String jsonRequest = objectMapper.writeValueAsString(newOffice);

        String jsonResponse = mockMvc.perform(post("/api/doctorsOffice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.roomNumber").value("101"))
                .andReturn().getResponse().getContentAsString();

        DoctorsOfficeDetailsDTO created = objectMapper.readValue(jsonResponse, DoctorsOfficeDetailsDTO.class);

        mockMvc.perform(get("/api/doctorsOffice/" + created.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomDescription").value("Gabinet RTG"));
    }

    @Test
    void shouldReturnAllDoctorsOffices() throws Exception {
        doctorsOfficeRepository.save(new DoctorsOffice("201", "A"));
        doctorsOfficeRepository.save(new DoctorsOffice("202", "B"));

        mockMvc.perform(get("/api/doctorsOffice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].roomNumber").exists())
                .andExpect(jsonPath("$[1].roomNumber").exists());
    }

    @Test
    void shouldDeleteDoctorsOffice() throws Exception {
        DoctorsOffice saved = doctorsOfficeRepository.save(new DoctorsOffice("303", "Do usunięcia"));

        mockMvc.perform(delete("/api/doctorsOffice/" + saved.getId()))
                .andExpect(status().isNoContent());

        assertTrue(doctorsOfficeRepository.findById(saved.getId()).isEmpty());
    }

    @Test
    void shouldNotAllowDuplicateRoomNumber() {
        CreateDoctorsOfficeDTO office1 = new CreateDoctorsOfficeDTO("100", "Gabinet A");
        CreateDoctorsOfficeDTO office2 = new CreateDoctorsOfficeDTO("100", "Gabinet B (duplikat numeru)");

        doctorsOfficeService.addDoctorsOffice(office1);

        assertThrows(DataIntegrityViolationException.class, () -> {
            doctorsOfficeService.addDoctorsOffice(office2);
            doctorsOfficeRepository.flush();
        });
    }

    @Test
    void shouldValidateInputData() throws Exception {
        String invalidJson = "{}";

        mockMvc.perform(post("/api/doctorsOffice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}