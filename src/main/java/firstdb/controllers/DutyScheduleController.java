package firstdb.controllers;


import firstdb.services.schedule.DutySchedule;
import firstdb.services.DutyScheduleService;
import firstdb.model.User;
import firstdb.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.DayOfWeek;
import java.time.LocalDate;

import java.util.*;

@Controller
public class DutyScheduleController {

    @Autowired
    private DutyScheduleService scheduleService;

    @Autowired
    private UserService userService;

    @Autowired
    public DutyScheduleController(DutyScheduleService scheduleService, UserService userService) {
        this.scheduleService = scheduleService;
        this.userService = userService;
    }

    @GetMapping("/schedule")
    public String showSchedule(Model model, @RequestParam(defaultValue = "0") int page) {
        int pageSize = 10;
        List<DutySchedule> allSchedules = scheduleService.getAllSchedules();
        List<User> participants = userService.getAllUsers();

        int totalSchedules = allSchedules.size();
        int totalPages = (int) Math.ceil((double) totalSchedules / pageSize);

        if (page >= totalPages) {
            page = totalPages - 1;
        }
        if (page < 0) {
            page = 0;
        }

        int startItem = page * pageSize;
        List<DutySchedule> schedules;

        if (allSchedules.size() < startItem) {
            schedules = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, allSchedules.size());
            schedules = allSchedules.subList(startItem, toIndex);
        }

        model.addAttribute("schedules", schedules);
        model.addAttribute("participants", participants);
        model.addAttribute("newSchedule", new DutySchedule());
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);

        return "schedule";
    }

    @PostMapping("/schedule")
    public String addNewSchedule(@ModelAttribute("newSchedule") DutySchedule schedule) {
        if (schedule.getParticipant() != null) {

            User participant = userService.findById(schedule.getParticipant().getId());
            if (participant != null) {
                schedule.setParticipant(participant);
            }
        }
        scheduleService.save(schedule);
        return "redirect:/calendar_view";
    }

    @GetMapping("/calendar_view")
    public String viewCalendar(Model model) {
        List<DutySchedule> dutySchedules = scheduleService.getAllSchedules();
        List<User> participants = userService.getAllUsers();

        if (dutySchedules.isEmpty() && !participants.isEmpty()) {
            LocalDate startDate = LocalDate.now().withDayOfYear(1);
            LocalDate endDate = startDate.withDayOfYear(startDate.lengthOfYear());
            int userCount = participants.size();
            int dayCounter = 0;

            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    continue;
                }

                User participant = participants.get(dayCounter % userCount);
                DutySchedule dutySchedule = new DutySchedule();
                dutySchedule.setDate(date);
                dutySchedule.setParticipant(participant);
                dutySchedule.setDutyType("Küche");

                scheduleService.save(dutySchedule);
                dayCounter++;
            }
        }
        dutySchedules = scheduleService.getAllSchedules();

        List<Map<String, String>> events = new ArrayList<>();
        for (DutySchedule schedule : dutySchedules) {
            if (schedule.getParticipant() != null) {
                Map<String, String> event = new HashMap<>();
                event.put("title", schedule.getDutyType() + " - "  + schedule.getParticipant().getFullname());
                event.put("start", schedule.getDate().toString());
                events.add(event);
            }
        }

        model.addAttribute("events", events);
        model.addAttribute("schedules", dutySchedules);
        model.addAttribute("participants", participants);
        model.addAttribute("newSchedule", new DutySchedule());

        return "calendar_view";
    }


    @GetMapping("/delete_schedule/{id}")
    public String deleteSchedule(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            scheduleService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Duty schedule successfully deleted.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "An error occurred while deleting the duty schedule.");
        }
        return "redirect:/schedule";
    }


    @GetMapping("/update_schedule/{id}")
    public String editSchedule(@PathVariable Long id, Model model) {
        DutySchedule schedule = scheduleService.findById(id);
        model.addAttribute("schedule", schedule);
        return "update_schedule";
    }

    @PostMapping("/update_schedule/{id}")
    public String updateSchedule(@PathVariable Long id, @ModelAttribute("schedule") DutySchedule updatedSchedule) {
        DutySchedule existingSchedule = scheduleService.findById(id);
        existingSchedule.setDutyType(updatedSchedule.getDutyType());
        scheduleService.save(existingSchedule);
        return "redirect:/schedule";
    }

    @GetMapping("/api/calendar-events")
    @ResponseBody
    public List<Map<String, String>> getCalendarEvents() {
        List<DutySchedule> dutySchedules = scheduleService.getAllSchedules();
        List<Map<String, String>> events = new ArrayList<>();

        for (DutySchedule schedule : dutySchedules) {
            if (schedule.getParticipant() != null) {
                Map<String, String> event = new HashMap<>();
                event.put("title", schedule.getDutyType() + " - "  + schedule.getParticipant().getFullname());
                event.put("start", schedule.getDate().toString());
                events.add(event);
            }
        }
        return events;
    }
    @PostMapping("/schedule/deleteAll")
    public String deleteAllSchedules() {
        scheduleService.deleteAllSchedules();
        return "redirect:/schedule";
    }
    @PostMapping("/schedule/manualAssignForYear")
    public String manualAssignForYear(@RequestParam Long participantId, @RequestParam String dayOfWeek) {
        User participant = userService.findById(participantId);
        DayOfWeek targetDay = DayOfWeek.valueOf(dayOfWeek.toUpperCase());

        LocalDate startDate = LocalDate.now().withDayOfYear(1);
        LocalDate endDate = startDate.withDayOfYear(startDate.lengthOfYear());

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {

            if (date.getDayOfWeek().equals(targetDay)) {
                DutySchedule dutySchedule = new DutySchedule();
                dutySchedule.setDate(date);
                dutySchedule.setParticipant(participant);
                dutySchedule.setDutyType("Küche");

                scheduleService.save(dutySchedule);
            }
        }
        return "redirect:/calendar_view";
    }
}