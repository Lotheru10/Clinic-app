package pl.edu.agh.to.clinicapp.appointment;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.to.clinicapp.dto.appointment_dto.AppointmentDTO;
import pl.edu.agh.to.clinicapp.dto.appointment_dto.CreateAppointmentDTO;

@RestController
@RequestMapping(path ="api/appointments")
@Tag(name = "Appointments", description = "API for managing appointments")
public class AppointmentController {


    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentDTO addAppointment(@Valid @RequestBody CreateAppointmentDTO createAppointmentDTO){
        return appointmentService.addAppointment(createAppointmentDTO);
    }
}
