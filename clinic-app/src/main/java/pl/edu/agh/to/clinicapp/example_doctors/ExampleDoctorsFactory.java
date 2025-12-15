package pl.edu.agh.to.clinicapp.example_doctors;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pl.edu.agh.to.clinicapp.doctor.Doctor;
import pl.edu.agh.to.clinicapp.doctor.Specialization;

import java.util.List;

@Configuration
@Profile("dev")
public class ExampleDoctorsFactory {

    @Bean
    public List<Doctor> exampleDoctors() {
        return List.of(
                new Doctor("Luke", "Skywalker", "90010112345", Specialization.KARDIOLOGIA, "Warszawa"),
                new Doctor("Bruce", "Wayne", "92030354321", Specialization.KARDIOLOGIA, "Kraków"),
                new Doctor("Gordon", "Freeman", "85051598765", Specialization.KARDIOLOGIA, "Gdańsk"),
                new Doctor("Gabe", "Newell", "78022011223", Specialization.OKULISTYKA, "Wrocław"),
                new Doctor("Alyx", "Vance", "95111133445", Specialization.OKULISTYKA, "Poznań"),
                new Doctor("Hideo", "Kojima", "88080877777", Specialization.CHIRURGIA_OGOLNA, "Szczecin"),
                new Doctor("Mark", "Grayson", "99121266666", Specialization.PEDIATRIA, "Lublin")
        );
    }
}
