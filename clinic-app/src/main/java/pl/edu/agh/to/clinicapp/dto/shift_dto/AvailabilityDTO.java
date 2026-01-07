package pl.edu.agh.to.clinicapp.dto.shift_dto;

import pl.edu.agh.to.clinicapp.doctors_office.DoctorsOffice;
import pl.edu.agh.to.clinicapp.dto.doctor_dto.DoctorDTO;
import pl.edu.agh.to.clinicapp.dto.doctors_office_dto.DoctorsOfficeDTO;

import java.util.List;
import java.util.Set;

public record AvailabilityDTO(
        Set<DoctorDTO> freeDoctors,
        Set<DoctorsOfficeDTO> freeDoctorsOffices
) {
}
