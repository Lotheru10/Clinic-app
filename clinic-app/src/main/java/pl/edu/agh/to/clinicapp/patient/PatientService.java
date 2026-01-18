package pl.edu.agh.to.clinicapp.patient;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pl.edu.agh.to.clinicapp.dto.patient_dto.CreatePatientDTO;
import pl.edu.agh.to.clinicapp.dto.patient_dto.PatientDTO;
import pl.edu.agh.to.clinicapp.dto.patient_dto.PatientDetailsDTO;
import pl.edu.agh.to.clinicapp.exception.patient_exceptions.PatientNotFoundException;

import java.util.List;

@Service
@Validated
public class PatientService {
    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    /**
     * Retrieves a list of all patients available in the system.
     * The results are mapped to a simplified DTO format.
     *
     * @return a list of {@link PatientDTO} objects representing all patients
     */
    public List<PatientDTO> getPatients(){
        return patientRepository.findAll()
                .stream()
                .map(this::mapToPatientDTO)
                .toList();
    }

    /**
     * Retrieves detailed information about a specific patient by their ID.
     *
     * @param id the unique identifier of the patient
     * @return a {@link PatientDetailsDTO} containing detailed information including address
     * @throws PatientNotFoundException if no patient is found with the provided ID
     */
    public PatientDetailsDTO getPatientById(int id){
        return patientRepository.findById(id)
                .map(this::mapToPatientDetailsDTO)
                .orElseThrow(()-> new PatientNotFoundException(id));
    }

    private PatientDTO mapToPatientDTO(Patient patient) {
        return new PatientDTO(
                patient.getId(),
                patient.getFirstName(),
                patient.getLastName()
        );
    }

    private PatientDetailsDTO mapToPatientDetailsDTO(Patient patient) {
        return new PatientDetailsDTO(
                patient.getId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getAddress()
        );
    }

    /**
     * Registers a new patient in the system.
     * <p>
     * This method maps the provided creation DTO to a Patient entity, persists it
     * to the database within a transaction, and returns the saved patient as a DTO.
     * Input data is validated before processing.
     * </p>
     * @param createPatientDTO the DTO containing the new patient's details (e.g. PESEL, specialization); must be valid
     * @return a {@link PatientDetailsDTO} representing the newly created patient, including the generated ID
     */
    @Transactional
    public PatientDetailsDTO addPatient(@Valid CreatePatientDTO createPatientDTO) {
        Patient patient = new Patient();
        patient.setFirstName(createPatientDTO.firstName());
        patient.setLastName(createPatientDTO.lastName());
        patient.setPeselNumber(createPatientDTO.peselNumber());
        patient.setAddress(createPatientDTO.address());

        Patient savedPatient = patientRepository.save(patient);

        return mapToPatientDetailsDTO(savedPatient);
    }

    /**
     * Deletes a patient from the system based on their ID.
     *
     * @param id the unique identifier of the patient to be deleted
     */
    public void deletePatient(int id){
        patientRepository.deleteById(id);
    }
}
