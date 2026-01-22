package pl.edu.agh.to.clinicapp.opinion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpinionRepository extends JpaRepository<Opinion, Integer> {
    boolean existsByAppointmentId(int appointmentId);


    @Query("""
            select o from Opinion o
            join o.appointment a
            where a.shift.doctor.id = :doctorId
            """)
    List<Opinion> findOpinionsByDoctorId(
            @Param("doctorId") int doctorId
    );
}
