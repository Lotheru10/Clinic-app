package pl.edu.agh.to.clinicapp.example_doctors;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.to.clinicapp.doctor.Doctor;
import pl.edu.agh.to.clinicapp.doctor.DoctorRepository;

import java.util.List;

@RestController
@RequestMapping("api/dev/doctors")
@Profile("dev")
public class ExampleDoctorController {
    private final DoctorRepository doctorRepository;
    private final List<Doctor> exampleDoctors;

    public ExampleDoctorController(DoctorRepository doctorRepository, List<Doctor> exampleDoctors) {
        this.doctorRepository = doctorRepository;
        this.exampleDoctors = exampleDoctors;
    }

    @PostMapping("/add-doctors")
    public void addExampleDoctors() {
        doctorRepository.saveAll(exampleDoctors);
    }



}
