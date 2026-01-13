package pl.edu.agh.to.clinicapp.doctors_office;

import jakarta.persistence.*;
import pl.edu.agh.to.clinicapp.shift.Shift;

import java.util.ArrayList;
import java.util.List;

@Entity
public class DoctorsOffice {
    @Id
    @GeneratedValue
    private int id;

    @Column(nullable = false, unique = true)
    private String roomNumber;

    @Column(nullable = false)
    private String roomDescription;


    @OneToMany(mappedBy = "office")
    private final List<Shift> shifts = new ArrayList<>();

    public DoctorsOffice(String roomNumber, String roomDescription){
        this.roomNumber = roomNumber;
        this.roomDescription = roomDescription;
    }

    public DoctorsOffice(){

    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getRoomDescription() {
        return roomDescription;
    }

    public List<Shift> getShifts() {
        return shifts;
    }

    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
}
