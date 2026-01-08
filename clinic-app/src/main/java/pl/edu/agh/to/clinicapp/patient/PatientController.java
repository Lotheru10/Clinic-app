package pl.edu.agh.to.clinicapp.patient;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.to.clinicapp.dto.patient_dto.CreatePatientDTO;
import pl.edu.agh.to.clinicapp.dto.patient_dto.PatientDTO;
import pl.edu.agh.to.clinicapp.dto.patient_dto.PatientDetailsDTO;

import java.util.List;

@RestController
@RequestMapping(path="api/patients")
@Tag(name = "Patients", description = "API for managing patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @Operation(
            summary = "Get patient details by ID",
            description = "Retrieves detailed information about a patient including their address."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient found"),
            @ApiResponse(responseCode = "404", description = "Patient with provided ID not found")
    })
    @GetMapping("/{id}")
    public PatientDetailsDTO getPatientById(
            @Parameter(description = "ID of the patient to be retrieved") @PathVariable("id") int id) {
        return patientService.getPatientById(id);
    }

    @Operation(
            summary = "Get all patients",
            description = "Retrieves a list of all patients with basic information."
    )
    @ApiResponse(responseCode = "200", description = "List of patients retrieved successfully")
    @GetMapping
    public List<PatientDTO> getPatients(){
        return patientService.getPatients();
    }

    @Operation(
            summary = "Register a new patient",
            description = "Creates a new patient record in the system."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Patient created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PatientDetailsDTO addPatient(@Valid @RequestBody CreatePatientDTO createPatientDTO){
        return patientService.addPatient(createPatientDTO);
    }

    @Operation(
            summary = "Delete a patient",
            description = "Removes a patient from the system based on their ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient deleted successfully"),
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePatient(
            @Parameter(description = "ID of the patient to delete") @PathVariable("id") int id){
        patientService.deletePatient(id);
    }
}
