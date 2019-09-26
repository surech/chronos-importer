package ch.surech.chronos.chronosimporter.model;

import com.microsoft.graph.models.generated.AttendeeType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "Participant")
@Getter @Setter
@NoArgsConstructor
public class Participant {

    @Id
    @Column(name = "participant_pk")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "attendee_type")
    @Enumerated(EnumType.STRING)
    private AttendeeType attendeeType;

    @ManyToOne
    @JoinColumn(name = "event_fk")
    private ImportedEvent event;
}
