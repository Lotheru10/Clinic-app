package pl.edu.agh.to.clinicapp.shift;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Integer> {
    @Query("""
            select (count(*)>0) from Shift s
            where s.doctor.id = :doctorId
            and s.start < :end
            and s.end > :start
            """)
    boolean doctorBusy(
            @Param("doctorId") int doctorId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
            );

    @Query("""
            select (count(*)>0) from Shift s
            where s.office.id = :doctorsOfficeId
            and s.start < :end
            and s.end > :start
            """)
    boolean doctorsOfficeBusy(
            @Param("doctorsOfficeId") int doctorsOfficeId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("""
            select s from Shift s
            where s.start < :end
            and s.end > :start
            """)
    List<Shift> overlappingShifts(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
