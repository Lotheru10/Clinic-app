package pl.edu.agh.to.clinicapp.dto.doctor_dto;

import pl.edu.agh.to.clinicapp.doctor.Specialization;
import pl.edu.agh.to.clinicapp.dto.shift_dto.DoctorShiftDTO;

import java.util.List;

public record DoctorDetailsDTO(
        int id,
        String firstName,
        String lastName,
        Specialization specialization,
        String address,
        List<DoctorShiftDTO> shifts
) {}
