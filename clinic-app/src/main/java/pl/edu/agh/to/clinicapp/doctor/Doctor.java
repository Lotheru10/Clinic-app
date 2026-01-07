package pl.edu.agh.to.clinicapp.doctor;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import pl.edu.agh.to.clinicapp.shift.Shift;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String firstName;


    @Column(nullable = false)
    private String lastName;


    @Column(nullable = false, unique = true)
    private String peselNumber;

    @NotNull
    @Column(nullable = false)
    @Enumerated(jakarta.persistence.EnumType.STRING)
    private Specialization specialization;

    @Column(nullable = false)
    private String address;

    @OneToMany(mappedBy = "doctor" )
    private final List<Shift> shifts = new ArrayList<>();


    public Doctor(String firstName, String lastName, String peselNumber, Specialization specialization, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.peselNumber = peselNumber;
        this.specialization = specialization;
        this.address = address;
    }

    public Doctor() {

    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPeselNumber() {
        return peselNumber;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public String getAddress() {
        return address;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPeselNumber(String peselNumber) {
        this.peselNumber = peselNumber;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setId(int id) {
        this.id = id;
    }
}
