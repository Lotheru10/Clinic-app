package pl.edu.agh.to.clinicapp.shift;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.to.clinicapp.dto.shift_dto.CreateShiftDTO;
import pl.edu.agh.to.clinicapp.dto.shift_dto.ShiftDTO;

@RestController
@RequestMapping(path="api/shifts")
@Tag(name = "Shifts", description = "API for managing shifts")
public class ShiftController {
    private final ShiftService shiftService;


    public ShiftController(ShiftService shiftService) {
        this.shiftService = shiftService;
    }

    @Operation(
            summary = "Creates a new shift",
            description = "Creates a new shift record in a system for given doctor, doctor's office and time"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Shift created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShiftDTO addShift(@Valid @RequestBody CreateShiftDTO createShiftDTO) {
        return shiftService.addShift(createShiftDTO);
    }
}
