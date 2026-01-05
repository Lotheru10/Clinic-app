package pl.edu.agh.to.clinicapp.doctors_office;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import pl.edu.agh.to.clinicapp.dto.doctors_office_dto.CreateDoctorsOfficeDTO;
import pl.edu.agh.to.clinicapp.dto.doctors_office_dto.DoctorsOfficeDTO;
import pl.edu.agh.to.clinicapp.dto.doctors_office_dto.DoctorsOfficeDetailsDTO;
import pl.edu.agh.to.clinicapp.exception.DoctorsOfficeNotFoundException;

import java.util.List;

@Validated
@Service
public class DoctorsOfficeService {
    private final DoctorsOfficeRepository doctorsOfficeRepository;

    public DoctorsOfficeService(DoctorsOfficeRepository doctorsOfficeRepository) {
        this.doctorsOfficeRepository = doctorsOfficeRepository;
    }

    public DoctorsOfficeDetailsDTO getDoctorsOfficeById(int id){
        return doctorsOfficeRepository.findById(id)
                .map(this::mapToDoctorsOfficeDetailsDTO)
                .orElseThrow(() -> new DoctorsOfficeNotFoundException(id));
    }

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
                doctorsOffice.getRoomDescription()
        );
    }

    public void deleteDoctorsOffice(int id){
        doctorsOfficeRepository.deleteById(id);
    }
}
