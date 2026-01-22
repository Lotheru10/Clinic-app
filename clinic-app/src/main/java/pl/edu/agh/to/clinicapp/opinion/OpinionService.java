package pl.edu.agh.to.clinicapp.opinion;

import org.springframework.stereotype.Service;
import pl.edu.agh.to.clinicapp.appointment.Appointment;
import pl.edu.agh.to.clinicapp.appointment.AppointmentRepository;
import pl.edu.agh.to.clinicapp.exception.appointment_exceptions.AppointmentHasOpinionAlreadyException;
import pl.edu.agh.to.clinicapp.dto.opinion_dto.CreateOpinionDTO;
import pl.edu.agh.to.clinicapp.exception.appointment_exceptions.AppointmentNotFoundException;

import java.util.List;

@Service
public class OpinionService {
    private final OpinionRepository opinionRepository;
    private final AppointmentRepository appointmentRepository;

    public OpinionService(OpinionRepository opinionRepository, AppointmentRepository appointmentRepository) {
        this.opinionRepository = opinionRepository;
        this.appointmentRepository = appointmentRepository;
    }


    //List<String> or void and words in exception?? optional?
    public void addOpinion(CreateOpinionDTO createOpinionDTO){
        int rate = createOpinionDTO.rate();
        String message = createOpinionDTO.message();
        int appointmentId = createOpinionDTO.appointmentId();

        if (rate>5 || rate<1){
            throw new IllegalArgumentException("Rate should be in range od 1-5");
        }

        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(
                () -> new AppointmentNotFoundException(appointmentId)
        );

        if(opinionRepository.existsByAppointmentId(appointmentId)){
            throw new AppointmentHasOpinionAlreadyException(appointmentId);
        }

        //Validate message

        Opinion opinion = new Opinion(rate, message, appointment);
        opinionRepository.save(opinion);
    }
}
