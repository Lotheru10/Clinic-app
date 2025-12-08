package pl.edu.agh.to.clinicapp.doctor;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.edu.agh.to.clinicapp.dto.DoctorDTO;
import pl.edu.agh.to.clinicapp.dto.DoctorDetailsDTO;

import java.util.List;

@Service
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
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor at given ID not found"));
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
                doctor.getId(),
                doctor.getFirstName(),
                doctor.getLastName(),
                doctor.getSpecialization(),
                doctor.getAddress()
        );
    }


    @Transactional //will be needed in the future I think
    public Doctor addDoctor(Doctor doctor){
        return doctorRepository.save(doctor);
    }

    public void deleteDoctor(@RequestBody Doctor doctor){
        doctorRepository.delete(doctor);
    }
}
