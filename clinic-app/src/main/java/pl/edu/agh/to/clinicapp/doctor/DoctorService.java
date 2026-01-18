package pl.edu.agh.to.clinicapp.doctor;

import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import pl.edu.agh.to.clinicapp.doctors_office.DoctorsOffice;
import pl.edu.agh.to.clinicapp.dto.doctor_dto.CreateDoctorDTO;
import pl.edu.agh.to.clinicapp.dto.doctor_dto.DoctorDTO;
import pl.edu.agh.to.clinicapp.dto.doctor_dto.DoctorDetailsDTO;
import pl.edu.agh.to.clinicapp.dto.shift_dto.DoctorShiftDTO;
import pl.edu.agh.to.clinicapp.exception.doctor_exceptions.DoctorHasShiftException;
import pl.edu.agh.to.clinicapp.exception.doctor_exceptions.DoctorNotFoundException;
import pl.edu.agh.to.clinicapp.shift.Shift;

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
                doctor.getId(),
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
                doctor.getAddress(),
                doctor.getShifts().stream()
                        .map(this::mapToDoctorShiftDTO)
                        .toList()
        );
    }

    private DoctorShiftDTO mapToDoctorShiftDTO(Shift shift) {
        DoctorsOffice o = shift.getOffice();
        return new DoctorShiftDTO(
                o.getId(),
                o.getRoomNumber()+ ". " + o.getRoomDescription(),
                shift.getStart(),
                shift.getEnd()
        );
    }
    /**
     * Registers a new doctor in the system.
     * <p>
     * This method maps the provided creation DTO to a Doctor entity, persists it
     * to the database within a transaction, and returns the saved doctor as a DTO.
     * Input data is validated before processing.
     * </p>
     * @param createDoctorDTO the DTO containing the new doctor's details (e.g. PESEL, specialization); must be valid
     * @return a {@link DoctorDetailsDTO} representing the newly created doctor, including the generated ID
     */
    @Transactional
    public DoctorDetailsDTO addDoctor(@Valid CreateDoctorDTO createDoctorDTO) {
        Doctor doctor = new Doctor();
        doctor.setFirstName(createDoctorDTO.firstName());
        doctor.setLastName(createDoctorDTO.lastName());
        doctor.setPeselNumber(createDoctorDTO.peselNumber());
        doctor.setSpecialization(createDoctorDTO.specialization());
        doctor.setAddress(createDoctorDTO.address());

        Doctor savedDoctor = doctorRepository.save(doctor);

        return mapToDoctorDetailsDTO(savedDoctor);
    }

    /**
     * Deletes a doctor from the system based on their ID.
     *
     * @param id the unique identifier of the doctor to be deleted
     */
    @Transactional
    public void deleteDoctor(int id){
        if(!getDoctorById(id).shifts().isEmpty()){
            throw new DoctorHasShiftException(id);
        }
        doctorRepository.deleteById(id);
    }
}
