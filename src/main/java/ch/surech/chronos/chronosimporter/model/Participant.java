package ch.surech.chronos.chronosimporter.model;

import com.microsoft.graph.models.extensions.ResponseStatus;
import com.microsoft.graph.models.generated.AttendeeType;
import com.microsoft.graph.models.generated.ResponseType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "participant")
@Getter @Setter
@Builder
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

    @Column(name = "response_status")
    @Enumerated(EnumType.STRING)
    private ResponseType responseStatus;
}
