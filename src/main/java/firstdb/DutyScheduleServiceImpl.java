package firstdb;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DutyScheduleServiceImpl implements DutyScheduleService {


    @Autowired
    private DutyScheduleRepository dutyScheduleRepository;
    @Autowired
    public DutyScheduleServiceImpl(DutyScheduleRepository dutyScheduleRepository) {
        this.dutyScheduleRepository = dutyScheduleRepository;
    }

    @Override
    public void save(DutySchedule dutySchedule) {
        dutyScheduleRepository.save(dutySchedule);
    }
    @Override
    public List<DutySchedule> findAll() {
        return dutyScheduleRepository.findAll();
    }
    @Override
    public List<DutySchedule> getAllSchedules() {
        return dutyScheduleRepository.findAll();
    }

    @Override
    public DutySchedule findById(Long id) {
        return dutyScheduleRepository.findById(id).orElse(null);
    }

    public void deleteSchedule(Long id) {
        try {
            if (dutyScheduleRepository.existsById(id)) {
                dutyScheduleRepository.deleteById(id);
            } else {
                throw new ResourceNotFoundException("Schedule not found with id " + id);
            }
        } catch (ResourceNotFoundException e) {

            e.printStackTrace();
        }
    }


    @Override
    public List<DutySchedule> findSchedulesBetween(LocalDate startDate, LocalDate endDate) {
        return dutyScheduleRepository.findByDateBetween(startDate, endDate);
    }
    @Override
    public void deleteById(Long id) {
        dutyScheduleRepository.deleteById(id);
    }

    public DutySchedule update(Long id, DutySchedule updatedSchedule) {
        return dutyScheduleRepository.findById(id)
                .map(schedule -> {
                    schedule.setParticipant(updatedSchedule.getParticipant());
                    schedule.setParticipant(updatedSchedule.getParticipant());
                    return dutyScheduleRepository.save(schedule);
                }).orElseThrow(() -> new EntityNotFoundException("Duty schedule not found"));
    }

}
