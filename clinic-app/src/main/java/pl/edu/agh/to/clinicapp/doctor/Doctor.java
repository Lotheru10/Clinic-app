package pl.edu.agh.to.clinicapp.doctor;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Doctor {

    @Id
    @GeneratedValue
    private int id;
    private String firstName;
    private String lastName;
    private String peselNumber;
    private String specialization;
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
