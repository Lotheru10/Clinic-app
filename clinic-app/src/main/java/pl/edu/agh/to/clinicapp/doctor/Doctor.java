package pl.edu.agh.to.clinicapp.doctor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
public class Doctor {

    @Id
    @GeneratedValue
    private int id;

    @NotBlank //at the Spring level
    @Column(nullable = false) //at the database level
    private String firstName;

    @NotBlank
    @Column(nullable = false)
    private String lastName;

    @NotBlank
    @Column(nullable = false, unique = true)
    @Pattern(regexp = "[0-9]{11}") //simple regex for pesel
    private String peselNumber;

    @NotBlank
    @Column(nullable = false)
    private String specialization;

    @NotBlank
    @Column(nullable = false)
    private String address;


    public Doctor(String firstName, String lastName, String peselNumber, String specialization, String address) {
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

    public String getSpecialization() {
        return specialization;
    }

    public String getAddress() {
        return address;
    }

}
