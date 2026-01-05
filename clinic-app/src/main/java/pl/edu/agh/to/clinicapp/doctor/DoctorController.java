package pl.edu.agh.to.clinicapp.doctor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.to.clinicapp.dto.doctor_dto.CreateDoctorDTO;
import pl.edu.agh.to.clinicapp.dto.doctor_dto.DoctorDTO;
import pl.edu.agh.to.clinicapp.dto.doctor_dto.DoctorDetailsDTO;

import java.util.List;

@RestController
@RequestMapping(path="api/doctors")
@Tag(name = "Doctors", description = "API for managing doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @Operation(
            summary = "Get doctor details by ID",
            description = "Retrieves detailed information about a doctor including their address."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor found"),
            @ApiResponse(responseCode = "404", description = "Doctor with provided ID not found")
    })
    @GetMapping("/{id}")
    public DoctorDetailsDTO getDoctorById(
            @Parameter(description = "ID of the doctor to be retrieved") @PathVariable int id) {
        return doctorService.getDoctorById(id);
    }

    @Operation(
            summary = "Get all doctors",
            description = "Retrieves a list of all doctors with basic information."
    )
    @ApiResponse(responseCode = "200", description = "List of doctors retrieved successfully")
    @GetMapping
    public List<DoctorDTO> getDoctors(){
        return doctorService.getDoctors();
    }

    @Operation(
            summary = "Register a new doctor",
            description = "Creates a new doctor record in the system."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Doctor created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorDetailsDTO addDoctor(@Valid @RequestBody CreateDoctorDTO createDoctorDTO){
        return doctorService.addDoctor(createDoctorDTO);
    }

    @Operation(
            summary = "Delete a doctor",
            description = "Removes a doctor from the system based on their ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor deleted successfully"),
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDoctor(
            @Parameter(description = "ID of the doctor to delete") @PathVariable int id){
        doctorService.deleteDoctor(id);
    }
}
