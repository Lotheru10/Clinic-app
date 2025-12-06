package pl.edu.agh.to.clinicapp.doctor;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<Doctor> getDoctors(){
        return doctorRepository.findAll();
    }

    @Transactional //will be needed in the future I think
    public Doctor addDoctor(Doctor doctor){
        return doctorRepository.save(doctor);
    }


}
