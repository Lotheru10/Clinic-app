package pl.edu.agh.to.clinicapp.doctors_office;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import pl.edu.agh.to.clinicapp.doctor.Doctor;
import pl.edu.agh.to.clinicapp.dto.doctors_office_dto.CreateDoctorsOfficeDTO;
import pl.edu.agh.to.clinicapp.dto.doctors_office_dto.DoctorsOfficeDTO;
import pl.edu.agh.to.clinicapp.dto.doctors_office_dto.DoctorsOfficeDetailsDTO;
import pl.edu.agh.to.clinicapp.dto.shift_dto.DoctorOfficeShiftDTO;
import pl.edu.agh.to.clinicapp.exception.doctor_office_exceptions.DoctorsOfficeHasShiftException;
import pl.edu.agh.to.clinicapp.exception.doctor_office_exceptions.DoctorsOfficeNotFoundException;
import pl.edu.agh.to.clinicapp.shift.Shift;

import java.util.List;

@Validated
@Service
public class DoctorsOfficeService {
    private final DoctorsOfficeRepository doctorsOfficeRepository;

    public DoctorsOfficeService(DoctorsOfficeRepository doctorsOfficeRepository) {
        this.doctorsOfficeRepository = doctorsOfficeRepository;
    }

    /**
     * Retrieves detailed information about a specific doctor's office by their ID.
     *
     * @param id the unique identifier of the doctor's office
     * @return a {@link DoctorsOfficeDetailsDTO} containing detailed information
     * @throws DoctorsOfficeNotFoundException if no doctor's office is found with the provided ID
     */
    public DoctorsOfficeDetailsDTO getDoctorsOfficeById(int id){
        return doctorsOfficeRepository.findById(id)
                .map(this::mapToDoctorsOfficeDetailsDTO)
                .orElseThrow(() -> new DoctorsOfficeNotFoundException(id));
    }

    /**
     * Retrieves a list of all doctors available in the system.
     * The results are mapped to a simplified DTO format.
     *
     * @return a list of {@link DoctorsOfficeDTO} objects representing all doctors
     */
    public List<DoctorsOfficeDTO> getAllDoctorsOffices() {
        return doctorsOfficeRepository.findAll()
                .stream()
                .map(this::mapToDoctorsOfficeDTO)
                .toList();
    }

    private DoctorsOfficeDTO mapToDoctorsOfficeDTO(DoctorsOffice doctorsOffice) {
        return new DoctorsOfficeDTO(
                doctorsOffice.getId(),
                doctorsOffice.getRoomNumber()
        );
    }

    /**
     * Registers a new doctor's office in the system.
     * <p>
     * This method maps the provided creation DTO to a DoctorsOffice entity, persists it
     * to the database within a transaction, and returns the saved doctor's office as a DTO.
     * Input data is validated before processing.
     * </p>
     * @param createDoctorsOfficeDTO the DTO containing the new doctor's office details; must be valid
     * @return a {@link DoctorsOfficeDetailsDTO} representing the newly created doctor's office, including the generated ID
     */
    @Transactional
    public DoctorsOfficeDetailsDTO addDoctorsOffice(@Valid CreateDoctorsOfficeDTO createDoctorsOfficeDTO){
        DoctorsOffice doctorsOffice = new DoctorsOffice();
        doctorsOffice.setRoomNumber(createDoctorsOfficeDTO.roomNumber());
        doctorsOffice.setRoomDescription(createDoctorsOfficeDTO.roomDescription());

        DoctorsOffice savedDoctorsOffice = doctorsOfficeRepository.save(doctorsOffice);

        return mapToDoctorsOfficeDetailsDTO(savedDoctorsOffice);
    }

    private DoctorsOfficeDetailsDTO mapToDoctorsOfficeDetailsDTO(DoctorsOffice doctorsOffice) {
        return new DoctorsOfficeDetailsDTO(
                doctorsOffice.getId(),
                doctorsOffice.getRoomNumber(),
                doctorsOffice.getRoomDescription(),
                doctorsOffice.getShifts().stream()
                        .map(this::mapToDoctorOfficeShiftDTO)
                        .toList()
        );
    }
    private DoctorOfficeShiftDTO mapToDoctorOfficeShiftDTO(Shift shift) {
        Doctor d = shift.getDoctor();
        return new DoctorOfficeShiftDTO(
                shift.getId(),
                d.getId(),
                d.getFirstName() + " "+ d.getLastName(),
                shift.getStart(),
                shift.getEnd()
        );
    }


    /**
     * Deletes a doctor's office from the system based on its ID.
     *
     * @param id the unique identifier of the doctor's office to be deleted
     */
    @Transactional
    public void deleteDoctorsOffice(int id){
        if(!getDoctorsOfficeById(id).shifts().isEmpty()) {
            throw new DoctorsOfficeHasShiftException(id);
        }
        doctorsOfficeRepository.deleteById(id);
    }
}
