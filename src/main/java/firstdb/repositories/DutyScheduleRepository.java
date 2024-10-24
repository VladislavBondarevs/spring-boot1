package firstdb.repositories;

import firstdb.services.schedule.DutySchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DutyScheduleRepository extends JpaRepository<DutySchedule, Long> {
    List<DutySchedule> findByDateBetween(LocalDate startDate, LocalDate endDate);
    void deleteAll();
    List<DutySchedule> findByDate(LocalDate date);
    DutySchedule findByParticipantIdAndDate(Long participantId, LocalDate date);
}
