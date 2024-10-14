package firstdb;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private TicketService ticketService;

    @MockBean
    private UserDetailsService userDetailsService;

    @InjectMocks
    private UserController userController;

    @Mock
    private Principal principal;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testRegisterSave_NewUser() throws Exception {
        UserDto userDto = new UserDto("newUser", "password","","","");
        when(userService.findByUsername(userDto.getUsername())).thenReturn(null);

        ResultActions resultActions = mockMvc.perform(post("/register")
                .flashAttr("user", userDto));

        resultActions.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register?success"));
    }

    @Test
    public void testCreateTicket() throws Exception {
        Ticket ticket = new Ticket();
        ResultActions resultActions = mockMvc.perform(post("/create-ticket")
                .flashAttr("ticket", ticket));

        resultActions.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    public void testViewReports() throws Exception {
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(new Ticket());
        tickets.add(new Ticket());
        when(ticketService.getAllTickets()).thenReturn(tickets);

        ResultActions resultActions = mockMvc.perform(get("/view-reports"));

        resultActions.andExpect(status().isOk())
                .andExpect(view().name("view_reports"))
                .andExpect(model().attributeExists("tickets"))
                .andExpect(model().attribute("tickets", tickets));
    }
}