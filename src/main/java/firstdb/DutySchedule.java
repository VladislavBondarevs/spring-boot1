package firstdb;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "duty_schedule")
public class DutySchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDate date;
    private String dutyType;
    @ManyToOne
    @JoinColumn(name = "participant_id")
    private User participant;
    private String status;

}
