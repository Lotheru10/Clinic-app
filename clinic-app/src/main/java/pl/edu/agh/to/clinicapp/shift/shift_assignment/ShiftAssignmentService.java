package pl.edu.agh.to.clinicapp.shift.shift_assignment;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.edu.agh.to.clinicapp.doctor.Doctor;
import pl.edu.agh.to.clinicapp.doctor.DoctorRepository;
import pl.edu.agh.to.clinicapp.doctors_office.DoctorsOffice;
import pl.edu.agh.to.clinicapp.doctors_office.DoctorsOfficeRepository;
import pl.edu.agh.to.clinicapp.dto.doctor_dto.DoctorDTO;
import pl.edu.agh.to.clinicapp.dto.doctors_office_dto.DoctorsOfficeDTO;
import pl.edu.agh.to.clinicapp.dto.shift_dto.AvailabilityDTO;
import pl.edu.agh.to.clinicapp.shift.Shift;
import pl.edu.agh.to.clinicapp.shift.ShiftRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class ShiftAssignmentService {
    private final ShiftRepository shiftRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorsOfficeRepository doctorsOfficeRepository;

    public ShiftAssignmentService(ShiftRepository shiftRepository, DoctorRepository doctorRepository, DoctorsOfficeRepository doctorsOfficeRepository) {
        this.shiftRepository = shiftRepository;
        this.doctorRepository = doctorRepository;
        this.doctorsOfficeRepository = doctorsOfficeRepository;
    }

    public AvailabilityDTO checkAvailability(LocalDateTime start, LocalDateTime end){
        if (start.isAfter(end) || start.isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("Shift starts in the past or after its end");
        }

        List<Shift> overlappingShifts = shiftRepository.overlappingShifts(start, end);

        Set<Integer> busyDoctors = new HashSet<>();
        Set<Integer> busyDoctorsOffices = new HashSet<>();

        overlappingShifts.forEach(shift -> {
            busyDoctors.add(shift.getDoctor().getId());
            busyDoctorsOffices.add(shift.getOffice().getId());
        });

        Set<DoctorDTO> freeDoctors = new HashSet<>();
        Set<DoctorsOfficeDTO> freeDoctorsOffices = new HashSet<>();

        List<Doctor> allDoctors = doctorRepository.findAll();
        List<DoctorsOffice> allDoctorsOffice = doctorsOfficeRepository.findAll();

        allDoctors.forEach(doctor -> {
                if (!busyDoctors.contains(doctor.getId())) {
                    freeDoctors.add(new DoctorDTO(doctor.getId(), doctor.getFirstName(), doctor.getLastName(), doctor.getSpecialization()));
                }
        });

        allDoctorsOffice.forEach(office -> {
            if (!busyDoctorsOffices.contains(office.getId())) {
                freeDoctorsOffices.add(new DoctorsOfficeDTO(office.getId(), office.getRoomNumber()));
            }
        });

        return new AvailabilityDTO(freeDoctors, freeDoctorsOffices);

    }
}
