package pl.edu.agh.to.clinicapp.appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    @Query("""
            select (count(*)>0) from Appointment a
                   where a.patient.id = :patientId
                   and a.start < :end
                   and a.end > :start
            """)
    boolean patientBusy(
            @Param("patientId") int patientId,
            @Param("start")LocalDateTime start,
            @Param("end") LocalDateTime end
            );

    @Query("""
        select (count(a) > 0) from Appointment a
            where a.shift.id = :shiftId
              and a.start < :end
              and a.end > :start
""")
    boolean shiftBusy(
            @Param("shiftId") int shiftId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("""
        select a from Appointment a
        where a.shift.id in :shiftIds
        order by a.start
        """)
    List<Appointment> findByShiftIds(@Param("shiftIds") Set<Integer> shiftIds);

}



