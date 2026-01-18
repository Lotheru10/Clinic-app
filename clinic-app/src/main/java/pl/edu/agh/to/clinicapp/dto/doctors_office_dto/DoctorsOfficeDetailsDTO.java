package pl.edu.agh.to.clinicapp.dto.doctors_office_dto;

import pl.edu.agh.to.clinicapp.dto.shift_dto.DoctorOfficeShiftDTO;

import java.util.List;

public record DoctorsOfficeDetailsDTO(
        int id,
        String roomNumber,
        String roomDescription,
        List<DoctorOfficeShiftDTO> shifts
) {
}
