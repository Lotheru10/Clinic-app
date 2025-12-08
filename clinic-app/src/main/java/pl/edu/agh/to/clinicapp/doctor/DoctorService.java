package pl.edu.agh.to.clinicapp.doctor;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class DoctorService {
    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public Doctor getDoctorById(int id)
    {
        return doctorRepository.findById(id).orElse(null);
    }

    public List<Doctor> getDoctors(){
        return doctorRepository.findAll();
    }

    @Transactional //will be needed in the future I think
    public Doctor addDoctor(@Valid Doctor doctor){
        return doctorRepository.save(doctor);
    }

    public void deleteDoctor(@RequestBody Doctor doctor){
        doctorRepository.delete(doctor);
    }
}
