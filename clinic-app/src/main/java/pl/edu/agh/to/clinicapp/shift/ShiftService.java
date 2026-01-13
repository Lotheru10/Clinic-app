package pl.edu.agh.to.clinicapp.shift;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.edu.agh.to.clinicapp.doctor.Doctor;
import pl.edu.agh.to.clinicapp.doctor.DoctorRepository;
import pl.edu.agh.to.clinicapp.doctors_office.DoctorsOffice;
import pl.edu.agh.to.clinicapp.doctors_office.DoctorsOfficeRepository;
import pl.edu.agh.to.clinicapp.dto.shift_dto.CreateShiftDTO;
import pl.edu.agh.to.clinicapp.dto.shift_dto.ShiftDTO;
import pl.edu.agh.to.clinicapp.exception.DoctorNotFoundException;
import pl.edu.agh.to.clinicapp.exception.DoctorsOfficeNotFoundException;

import java.time.LocalDateTime;

@Service
public class ShiftService {

    private final ShiftRepository shiftRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorsOfficeRepository doctorsOfficeRepository;

    public ShiftService(ShiftRepository shiftRepository, DoctorRepository doctorRepository, DoctorsOfficeRepository doctorsOfficeRepository) {
        this.shiftRepository = shiftRepository;
        this.doctorRepository = doctorRepository;
        this.doctorsOfficeRepository = doctorsOfficeRepository;
    }


    @Transactional
    public ShiftDTO addShift(CreateShiftDTO createShiftDTO){
        int doctorId = createShiftDTO.doctorId();
        int officeId = createShiftDTO.officeId();
        LocalDateTime start = createShiftDTO.start();
        LocalDateTime end = createShiftDTO.end();

        if (start.isAfter(end) || start.isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("Shift starts in the past or after its end");
        }

        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(
                () -> new DoctorNotFoundException(doctorId));
        DoctorsOffice doctorsOffice = doctorsOfficeRepository.findById(officeId).orElseThrow(
                () -> new DoctorsOfficeNotFoundException(officeId));

        if (shiftRepository.doctorBusy(doctorId, start, end)){
            throw new IllegalStateException("Doctor is busy");
        }
        if (shiftRepository.doctorsOfficeBusy(officeId, start, end)){
            throw new IllegalStateException("DoctorsOffice is busy");
        }
        Shift shift = new Shift(doctor, doctorsOffice, start, end);

        doctor.getShifts().add(shift);
        doctorsOffice.getShifts().add(shift);

        Shift saved = shiftRepository.save(shift);

        return new ShiftDTO(
                saved.getId(),
                saved.getDoctor().getId(),
                saved.getDoctor().getFirstName()+ " "+ saved.getDoctor().getLastName(),
                saved.getOffice().getId(),
                saved.getOffice().getRoomNumber() +". "+saved.getOffice().getRoomDescription(),
                saved.getStart(),
                saved.getEnd()
        );
    }
}
