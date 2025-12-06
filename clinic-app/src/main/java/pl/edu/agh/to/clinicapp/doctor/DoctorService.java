package pl.edu.agh.to.clinicapp.doctor;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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
    public Doctor addDoctor(Doctor doctor){
        return doctorRepository.save(doctor);
    }

    public void deleteDoctor(@RequestBody Doctor doctor){
        doctorRepository.delete(doctor);
    }
}
