package pl.edu.agh.to.clinicapp.doctors_office;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.to.clinicapp.dto.doctors_office_dto.CreateDoctorsOfficeDTO;
import pl.edu.agh.to.clinicapp.dto.doctors_office_dto.DoctorsOfficeDTO;
import pl.edu.agh.to.clinicapp.dto.doctors_office_dto.DoctorsOfficeDetailsDTO;

import java.util.List;

@RestController
@RequestMapping("/api/doctorsOffice")
@Tag(name="Doctor's office", description = "API for managing Doctor's Offices")
public class DoctorsOfficeController {

    private final DoctorsOfficeService doctorsOfficeService;

    public DoctorsOfficeController(DoctorsOfficeService doctorsOfficeService) {
        this.doctorsOfficeService = doctorsOfficeService;
    }

    @Operation(
            summary = "Get doctor's office details by ID",
            description = "Retrieves detailed information about a doctor's office."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor's office found"),
            @ApiResponse(responseCode = "404", description = "Doctor's office with provided ID not found")
    })
    @GetMapping("/{id}")
    public DoctorsOfficeDetailsDTO getDoctorsOfficeById( @Parameter(description = "ID of the doctor's office to be retrieved") @PathVariable int id){
        return doctorsOfficeService.getDoctorsOfficeById(id);
    }

    @Operation(
            summary = "Get all doctor's office",
            description = "Retrieves a list of all doctor's office with basic information."
    )
    @ApiResponse(responseCode = "200", description = "List of doctor's offices retrieved successfully")
    @GetMapping
    public List<DoctorsOfficeDTO> getAllDoctorsOffices(){
        return doctorsOfficeService.getAllDoctorsOffices();
    }

    @Operation(
            summary = "Register a new doctor's office",
            description = "Creates a new doctor's office record in the system."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Doctor's office created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorsOfficeDetailsDTO createDoctorsOffice(@Valid @RequestBody CreateDoctorsOfficeDTO createDoctorsOfficeDTO){
        return doctorsOfficeService.addDoctorsOffice(createDoctorsOfficeDTO);
    }

    @Operation(
            summary = "Delete a doctor's office",
            description = "Removes a doctor's office from the system based on its ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor's office deleted successfully"),
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDoctorsOffice(@Parameter(description = "ID of the doctor's office to delete") @PathVariable int id){
        doctorsOfficeService.deleteDoctorsOffice(id);
    }
}
