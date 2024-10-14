package firstdb;



import java.util.List;

public interface TicketService {
    List<Ticket> getAllTickets();
    Ticket getTicketById(Long id);
    void saveTicket(Ticket ticket);
    void deleteTicketById(Long id);

}
