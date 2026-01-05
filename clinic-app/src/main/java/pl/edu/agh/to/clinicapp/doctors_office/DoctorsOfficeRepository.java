package pl.edu.agh.to.clinicapp.doctors_office;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorsOfficeRepository extends JpaRepository<DoctorsOffice, Integer> {
}
