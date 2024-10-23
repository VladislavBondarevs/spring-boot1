package firstdb;

import java.time.LocalDate;
import java.util.List;

public interface DutyScheduleService {
    List<DutySchedule> getAllSchedules();
    DutySchedule findById(Long id);
    void deleteSchedule(Long id);
    List<DutySchedule> findSchedulesBetween(LocalDate startDate, LocalDate endDate);
    void save(DutySchedule dutySchedule);
    List<DutySchedule> findAll();
    void deleteById(Long id);

}
