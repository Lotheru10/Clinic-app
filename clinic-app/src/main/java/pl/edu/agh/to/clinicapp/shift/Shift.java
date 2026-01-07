package pl.edu.agh.to.clinicapp.shift;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import pl.edu.agh.to.clinicapp.doctor.Doctor;
import pl.edu.agh.to.clinicapp.doctor.Specialization;
import pl.edu.agh.to.clinicapp.doctors_office.DoctorsOffice;
import pl.edu.agh.to.clinicapp.dto.doctor_dto.DoctorDetailsDTO;
import pl.edu.agh.to.clinicapp.dto.doctors_office_dto.DoctorsOfficeDetailsDTO;

import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "shifts")
public class Shift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(optional = false)
    private Doctor doctor;

    @ManyToOne(optional = false)
    private DoctorsOffice office;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime start;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime end;

    public Shift() {
    }

    public Shift(Doctor doctor, DoctorsOffice office, LocalDateTime start, LocalDateTime end) {
        this.doctor = doctor;
        this.office = office;
        this.start = start;
        this.end = end;
    }


    public int getId() {
        return id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public DoctorsOffice getOffice() {
        return office;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public void setOffice(DoctorsOffice office) {
        this.office = office;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }
}
