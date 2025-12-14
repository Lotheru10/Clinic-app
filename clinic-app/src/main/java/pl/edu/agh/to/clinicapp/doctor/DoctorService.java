package pl.edu.agh.to.clinicapp.doctor;

import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import pl.edu.agh.to.clinicapp.dto.DoctorDTO;
import pl.edu.agh.to.clinicapp.dto.DoctorDetailsDTO;
import pl.edu.agh.to.clinicapp.exception.DoctorNotFoundException;

import java.util.List;

@Service
@Validated
public class DoctorService {
    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<DoctorDTO> getDoctors(){
        return doctorRepository.findAll()
                .stream()
                .map(this::mapToDoctorDTO)
                .toList();
    }

    public DoctorDetailsDTO getDoctorById(int id){
        return doctorRepository.findById(id)
                .map(this::mapToDoctorDetailsDTO)
                .orElseThrow(()-> new DoctorNotFoundException(id));
    }

    private DoctorDTO mapToDoctorDTO(Doctor doctor) {
        return new DoctorDTO(
                doctor.getFirstName(),
                doctor.getLastName(),
                doctor.getSpecialization()
        );
    }

    private DoctorDetailsDTO mapToDoctorDetailsDTO(Doctor doctor) {
        return new DoctorDetailsDTO(
                doctor.getFirstName(),
                doctor.getLastName(),
                doctor.getSpecialization(),
                doctor.getAddress()
        );
    }


    @Transactional //will be needed in the future I think
    public Doctor addDoctor(@Valid Doctor doctor){
        return doctorRepository.save(doctor);
    }

    public void deleteDoctor(int id){
        doctorRepository.deleteById(id);
    }
}
