package pl.edu.agh.to.clinicapp.appointment.appointment_assignment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.to.clinicapp.dto.appointment_dto.AvailableAppointmentSlotDTO;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/appointment-assigment")
@Tag(name = "Appointments assigment", description = "API for managing appointments assigment")
public class AppointmentAssingmentController {


    private final AppointmentAssingmentService appointmentAssingmentService;

    public AppointmentAssingmentController(AppointmentAssingmentService appointmentAssingmentService) {
        this.appointmentAssingmentService = appointmentAssingmentService;
    }


    @Operation(
            summary = "Returns available slots for an appointment",
            description = "Returns available slots for an appointment for a chosen doctor by id and for given start and end time with 15 minutes intervals in a flat list format")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Doctor not found")
    })

    @GetMapping("/availability")
    public List<AvailableAppointmentSlotDTO> getAvailability(
            @RequestParam("doctorId") int doctorId,
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ){
        return appointmentAssingmentService.getAvailableSlots(doctorId, start, end);
    }

}
