package pl.edu.agh.to.clinicapp.shift.shift_assignment;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.to.clinicapp.dto.shift_dto.AvailabilityDTO;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/shift-assignment")
public class ShiftAssignmentController {
    private final ShiftAssignmentService shiftAssignmentService;

    public ShiftAssignmentController(ShiftAssignmentService shiftAssignmentService) {
        this.shiftAssignmentService = shiftAssignmentService;
    }



    @GetMapping("/avaibility")
    public AvailabilityDTO getAvailability(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        return shiftAssignmentService.checkAvailability(start, end);
    }
}
