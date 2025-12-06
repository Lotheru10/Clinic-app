package pl.edu.agh.to.clinicapp.doctor;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DoctorConfigurator {

    private final DoctorRepository doctorRepository;

    public DoctorConfigurator(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @PostConstruct
    public void initDoctors(){
        if(doctorRepository.count() == 0){
            Doctor doctor = new Doctor("Janusz","Tracz","12345678","abc","abc");
            doctorRepository.save(doctor);
        }
    }
}
