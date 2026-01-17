package pl.edu.agh.to.clinicapp.appointment;

import jakarta.persistence.*;
import pl.edu.agh.to.clinicapp.patient.Patient;
import pl.edu.agh.to.clinicapp.shift.Shift;

import java.time.LocalDateTime;

@Entity
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Shift shift;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime start;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime end;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Patient patient;

    public Appointment(Shift shift, LocalDateTime start, LocalDateTime end, Patient patient) {
        this.shift = shift;
        this.start = start;
        this.end = end;
        this.patient = patient;
    }
    public Appointment(){
    }


    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }


    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
}
