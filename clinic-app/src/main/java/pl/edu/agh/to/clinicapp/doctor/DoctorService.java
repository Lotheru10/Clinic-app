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

    /**
     * Retrieves a list of all doctors available in the system.
     * The results are mapped to a simplified DTO format.
     *
     * @return a list of {@link DoctorDTO} objects representing all doctors
     */
    public List<DoctorDTO> getDoctors(){
        return doctorRepository.findAll()
                .stream()
                .map(this::mapToDoctorDTO)
                .toList();
    }

    /**
     * Retrieves detailed information about a specific doctor by their ID.
     *
     * @param id the unique identifier of the doctor
     * @return a {@link DoctorDetailsDTO} containing detailed information including address
     * @throws DoctorNotFoundException if no doctor is found with the provided ID
     */
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

    /**
     * Adds a new doctor to the database.
     * This operation is transactional.
     *
     * @param doctor the {@link Doctor} entity to be saved; must be valid according to constraints
     * @return the saved {@link Doctor} entity (including generated ID)
     */
    @Transactional
    public Doctor addDoctor(@Valid Doctor doctor){
        return doctorRepository.save(doctor);
    }

    /**
     * Deletes a doctor from the system based on their ID.
     *
     * @param id the unique identifier of the doctor to be deleted
     */
    public void deleteDoctor(int id){
        doctorRepository.deleteById(id);
    }
}
