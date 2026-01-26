package pl.edu.agh.to.clinicapp.opinion;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import pl.edu.agh.to.clinicapp.appointment.Appointment;
import pl.edu.agh.to.clinicapp.doctor.Doctor;

@Entity
public class Opinion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Max(5)
    @Min(1)
    private int rate;

    @Column()
    private String message;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Appointment appointment;

    public Opinion(int rate, String message, Appointment appointment) {
        this.rate = rate;
        this.message = message;
        this.appointment = appointment;
    }

    public Opinion(){

    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }
}
