package pl.edu.agh.to.clinicapp.appointment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "Add a new appointment",
            description = "Add a new appointment based on patientId, shiftId and start time.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Appointment successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Patient or shift not found"),
            @ApiResponse(responseCode = "409", description = "Appointment slot already taken")
    })


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentDTO addAppointment(@Valid @RequestBody CreateAppointmentDTO createAppointmentDTO){
        return appointmentService.addAppointment(createAppointmentDTO);
    }
}
