package pl.edu.agh.to.clinicapp.shift.shift_assignment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.to.clinicapp.dto.shift_dto.AvailabilityDTO;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/shift-assignment")
@Tag(name = "Shift assignment", description = "API for displaying free doctors and doctor's offices")
public class ShiftAssignmentController {
    private final ShiftAssignmentService shiftAssignmentService;

    public ShiftAssignmentController(ShiftAssignmentService shiftAssignmentService) {
        this.shiftAssignmentService = shiftAssignmentService;
    }


    @Operation(
            summary = "Returns available doctors and offices",
            description = "Returns list of free doctors and free doctor's offices in a given period of time")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "List retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
    })

    @GetMapping("/availability")
    public AvailabilityDTO getAvailability(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        return shiftAssignmentService.checkAvailability(start, end);
    }
}
