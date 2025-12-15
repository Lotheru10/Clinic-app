package pl.edu.agh.to.clinicapp.doctor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.to.clinicapp.dto.CreateDoctorDTO;
import pl.edu.agh.to.clinicapp.dto.DoctorDTO;
import pl.edu.agh.to.clinicapp.dto.DoctorDetailsDTO;

import java.util.List;

@RestController
@RequestMapping(path="api/doctors")
public class DoctorController {

    DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping("/{id}")
    public DoctorDetailsDTO getDoctorById(@PathVariable int id) {
        return doctorService.getDoctorById(id);
    }

    @GetMapping
    public List<DoctorDTO> getDoctors(){
        return doctorService.getDoctors();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorDTO addDoctor(@RequestBody CreateDoctorDTO createDoctorDTO){
        return doctorService.addDoctor(createDoctorDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteDoctor(@PathVariable int id){
        doctorService.deleteDoctor(id);
    }
}
