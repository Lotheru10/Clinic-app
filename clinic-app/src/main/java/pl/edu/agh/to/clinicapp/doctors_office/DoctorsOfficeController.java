package pl.edu.agh.to.clinicapp.doctors_office;


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

    @GetMapping("/{id}")
    public DoctorsOfficeDetailsDTO getDoctorsOfficeById(@PathVariable int id){
        return doctorsOfficeService.getDoctorsOfficeById(id);
    }
    @GetMapping
    public List<DoctorsOfficeDTO> getAllDoctorsOffices(){
        return doctorsOfficeService.getAllDoctorsOffices();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorsOfficeDetailsDTO createDoctorsOffice(@Valid @RequestBody CreateDoctorsOfficeDTO createDoctorsOfficeDTO){
        return doctorsOfficeService.addDoctorsOffice(createDoctorsOfficeDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDoctorsOffice(@PathVariable int id){
        doctorsOfficeService.deleteDoctorsOffice(id);
    }
}
