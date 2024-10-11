package firstdb;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AdminController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final UserServiceImpl userServiceImpl;
    private final TicketService ticketService;
    private final TicketRepository ticketRepository;
    @Autowired
    public AdminController(TicketRepository ticketRepository,UserService userService, UserServiceImpl userServiceImpl, UserRepository userRepository,TicketService ticketService) {
        this.userService = userService;
        this.userServiceImpl = userServiceImpl;
        this.userRepository = userRepository;
        this.ticketService = ticketService;
        this.ticketRepository = ticketRepository;
    }
    //Dashboard
    @GetMapping("/admin_dashboard")
    public String adminDashboard() {
        return "admin_dashboard";
    }

    @GetMapping("/manage_users")
    public String dashboard(Model model) {
        List<User> users = userServiceImpl.getAllUsers();
        model.addAttribute("users", users);
        return "manage_users";
    }

    //Manage Users
    @GetMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "redirect:/manage-users";
    }
    @PostMapping("/manage_users")
    public String addUser(@ModelAttribute("user") UserDto userDto) {
        userService.save(userDto);
        return "redirect:/manage_users";
    }

    @GetMapping("/manage-users")
    public String manageUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "manage_users";
    }

    //Edit Users

    @GetMapping("/edit-user/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
    User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
    model.addAttribute("user", user);
    return "edit_user";
    }
    @PostMapping("/edit-user/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute("user") User updatedUser) {
        userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        userRepository.save(updatedUser);
        return "redirect:/manage-users";
    }

    //View Tickets

    @GetMapping("/delete-ticket/{id}")
    public String deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicketById(id);
        return "redirect:/view-reports";
    }

    // Edit Tickets
    @GetMapping("/edit-ticket/{id}")
    public String editTicketForm(@PathVariable Long id, Model model) {
        Ticket ticket = ticketService.getTicketById(id);
        model.addAttribute("ticket", ticket);
        return "edit_tickets";
    }

    @PostMapping("/edit-ticket/{id}")
    public String updateTicket(@PathVariable Long id, @ModelAttribute("ticket") Ticket updatedTicket) {
        ticketService.getTicketById(id);
        ticketService.saveTicket(updatedTicket);
        return "redirect:/view-reports";
    }
}
