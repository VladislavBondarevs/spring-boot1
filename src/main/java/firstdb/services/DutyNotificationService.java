package firstdb.services;

import firstdb.model.User;
import firstdb.services.impl.DutyScheduleServiceImpl;
import firstdb.services.schedule.DutySchedule;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DutyNotificationService {

    @Autowired
    private DutyScheduleServiceImpl scheduleService;

    @Autowired
    private EmailService emailService;


    @Scheduled(cron = "0 0 8 * * ?")
    public void sendDailyDutyNotifications() {
        LocalDate today = LocalDate.now();
        List<DutySchedule> todaySchedules = scheduleService.getSchedulesForDate(today);

        for (DutySchedule schedule : todaySchedules) {
            User participant = schedule.getParticipant();
            if (participant != null) {
                String email = participant.getEmail();
                String subject = "Ihr Küchendienst heute!";
                String text = "Sehr geehrte(r) " + participant.getFullname() + ",\n\n"
                        + "Wir möchten Sie daran erinnern, dass Sie heute, am " + today + ", für den Küchendienst eingeteilt sind.";

                try {
                    emailService.sendEmail(email, subject, text);
                } catch (MessagingException e) {
                    System.err.println("Fehler beim senden " + e.getMessage());
                }
            }
        }
    }
}
