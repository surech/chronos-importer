package ch.surech.chronos.chronosimporter.model;

import com.microsoft.graph.models.extensions.ResponseStatus;
import com.microsoft.graph.models.generated.AttendeeType;
import com.microsoft.graph.models.generated.ResponseType;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "participant")
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Participant {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
