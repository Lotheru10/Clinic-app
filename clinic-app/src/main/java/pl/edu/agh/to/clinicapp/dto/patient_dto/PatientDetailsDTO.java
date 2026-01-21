package pl.edu.agh.to.clinicapp.dto.patient_dto;

import pl.edu.agh.to.clinicapp.appointment.Appointment;
import pl.edu.agh.to.clinicapp.dto.appointment_dto.AppointmentDTO;

import java.util.List;

public record PatientDetailsDTO(
        int id,
        String firstName,
        String lastName,
        String address,
        List<AppointmentDTO> appointments
) {}
