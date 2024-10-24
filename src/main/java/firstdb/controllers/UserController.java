package firstdb.controllers;

import java.io.IOException;
import java.security.Principal;

import java.util.List;

import firstdb.model.Ticket;
import firstdb.services.EmailService;
import firstdb.services.TicketService;
import firstdb.services.UserService;
import firstdb.model.User;
import firstdb.services.user.UserDto;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @Autowired
    private EmailService emailService;
    @Autowired
    private UserDetailsService userDetailsService;
    private final TicketService ticketService;
    private final UserService userService;

    public UserController(UserService userService, TicketService ticketService, UserDetailsService userDetailsService) {
        this.userService = userService;
        this.ticketService = ticketService;
        this.userDetailsService = userDetailsService;

    }

    @GetMapping("/home")
    public String home(Model model, Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail", userDetails);
        return "home";
    }

    @GetMapping("/login")
    public String login(Model model, UserDto userDto) {

        model.addAttribute("user", userDto);
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model, UserDto userDto) {
        model.addAttribute("user", userDto);
        return "register";
    }

    @PostMapping("/register")
    public String registerSave(@ModelAttribute("user") UserDto userDto, Model model) {
        String username = userDto.getUsername().trim();
        String fullName = userDto.getFullname().trim();

        if (username.isEmpty()) {
            model.addAttribute("usernameError", "Username cannot be empty or contain only spaces");
            return "register";
        }
        if (fullName.isEmpty()) {
            model.addAttribute("fullnameError", "Fullname cannot be empty or contain only spaces");
            return "register";
        }

        User user = userService.findByUsername(username);
        if (user != null) {
            model.addAttribute("userExistError", "User with this username already exists");
            return "register";
        }

        if ("ADMIN".equalsIgnoreCase(userDto.getRole())) {
            model.addAttribute("roleError", "You cannot assign yourself as ADMIN");
            return "register";
        }

        userService.save(userDto);

        try {
            emailService.sendEmail(userDto.getEmail(), "Welcome " + fullName, "Danke für die Registrierung, " + fullName + "!");
        } catch (Exception e) {
            model.addAttribute("emailError", "Error sending email: " + e.getMessage());
            return "register";
        }

        return "redirect:/register?success";
    }

    @GetMapping("/create_ticket")
    public String createTicketForm(Model model) {
        model.addAttribute("ticket", new Ticket());
        return "create_ticket";
    }

    @PostMapping("/create-ticket")
    public String createTicket(@ModelAttribute("ticket") Ticket newTicket) {
        ticketService.saveTicket(newTicket);
        return "redirect:/home";
    }

    @GetMapping("/view-reports")
    public String viewReports(Model model) {
        List<Ticket> tickets = ticketService.getAllTickets();
        model.addAttribute("tickets", tickets);
        return "view_reports";
    }
    // EMAIL SERVICE

    @GetMapping("/send-email")
    public String sendEmail(@RequestParam String email, @RequestParam String subject, @RequestParam String message, Model model) {
        try {
            emailService.sendEmail(email, subject, message);
            model.addAttribute("successMessage", "Email sent successfully to " + email);
        } catch (MessagingException e) {
            model.addAttribute("errorMessage", "Failed to send email: " + e.getMessage());
        }
        return "emailStatus";
    }
}