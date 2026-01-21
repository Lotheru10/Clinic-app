package pl.edu.agh.to.clinicapp.appointment.appointment_assignment;

import org.springframework.stereotype.Service;
import pl.edu.agh.to.clinicapp.appointment.Appointment;
import pl.edu.agh.to.clinicapp.appointment.AppointmentRepository;
import pl.edu.agh.to.clinicapp.dto.appointment_dto.AvailableAppointmentSlotDTO;
import pl.edu.agh.to.clinicapp.shift.Shift;
import pl.edu.agh.to.clinicapp.shift.ShiftRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class AppointmentAssignmentService {

    private final AppointmentRepository appointmentRepository;
    private final ShiftRepository shiftRepository;
    private final static int DURATION = 15;


    public AppointmentAssignmentService(AppointmentRepository appointmentRepository, ShiftRepository shiftRepository) {
        this.appointmentRepository = appointmentRepository;
        this.shiftRepository = shiftRepository;
    }

    public List<AvailableAppointmentSlotDTO> getAvailableSlots(int doctorId, LocalDateTime start, LocalDateTime end){
        if (start.isAfter(end) || start.isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("Appointment starts in the past or after its end");
        }

        List<AvailableAppointmentSlotDTO> result = new ArrayList<>();
        List<Shift> shifts = shiftRepository.overlappingShiftsForDoctor(doctorId, start, end);
        if (shifts.isEmpty()) return List.of();

        Set<Integer> shiftsIds = new HashSet<>();

        shifts.forEach(shift -> {
            shiftsIds.add(shift.getId());
        });

        //take all appointments and map them to avoid n+1 query's
        List<Appointment> existingAppointments = appointmentRepository.findByShiftIds(shiftsIds);

        Map<Integer, Set<LocalDateTime>> busyStartsbyShift = new HashMap<>();
        for (Appointment appointment: existingAppointments){
            int shiftId = appointment.getShift().getId();
            busyStartsbyShift
                    .computeIfAbsent(shiftId, k -> new HashSet<>())
                    .add(appointment.getStart());
        }

        for (Shift shift: shifts){
            LocalDateTime slotStart = getLater(shift.getStart(), start);
            LocalDateTime slotEnd = getEarlier(shift.getEnd(), end);

            slotStart = ceilToDuration(slotStart);

            Set<LocalDateTime> busySlots = busyStartsbyShift.getOrDefault(shift.getId(), Set.of());

            while(!slotStart.plusMinutes(DURATION).isAfter(slotEnd)){
                if(!busySlots.contains(slotStart)){
                    result.add(new AvailableAppointmentSlotDTO(
                            shift.getId(),
                            doctorId,
                            shift.getOffice().getId(),
                            slotStart,
                            slotStart.plusMinutes(DURATION)
                    ));
                }
                slotStart = slotStart.plusMinutes(DURATION);
            }
        }
        return result;
    }
    private LocalDateTime getLater(LocalDateTime date1, LocalDateTime date2){
        return (date1.isAfter(date2)) ? date1 : date2;
    }
    private LocalDateTime getEarlier(LocalDateTime date1, LocalDateTime date2){
        return (date1.isAfter(date2)) ? date2 : date1;
    }
    private LocalDateTime ceilToDuration(LocalDateTime date){
        date = date.withSecond(0).withNano(0);
        int minutes = date.getMinute();
        int mod = minutes % DURATION;
        return (mod ==0) ? date : date.plusMinutes(DURATION - mod);
    }
}
