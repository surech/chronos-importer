package ch.surech.chronos.chronosimporter.model;

import com.microsoft.graph.models.generated.EventType;
import com.microsoft.graph.models.generated.FreeBusyStatus;
import com.microsoft.graph.models.generated.Importance;
import com.microsoft.graph.models.generated.LocationType;
import lombok.*;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "importedevent")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportedEvent {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "icaluid")
    private String iCalUId;

    @Column(name = "subject")
    private String subject;

    @Column(name = "body_preview")
    private String bodyPreview;

    @Column(name = "importance")
    @Enumerated(EnumType.STRING)
    private Importance importance;

    @Column(name = "start", columnDefinition = "TIMESTAMP")
    private ZonedDateTime start;

    @Column(name = "end", columnDefinition = "TIMESTAMP")
    private ZonedDateTime end;

    @Column(name = "location_name")
    private String locationName;

    @Column(name = "location_uri")
    private String locationUri;

    @Column(name = "location_type")
    @Enumerated(EnumType.STRING)
    private LocationType locationType;

    @Column(name = "location_id")
    private String locationId;

    @Column(name = "is_all_day")
    private boolean isAllDay;

    @Column(name = "is_cancelled")
    private boolean isCancelled;

    @Column(name = "is_organizer")
    private boolean isOrganizer;

    @Column(name = "series_master_id")
    private String seriesMasterId;

    @Column(name = "show_as")
    @Enumerated(EnumType.STRING)
    private FreeBusyStatus showAs;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private EventType type;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    private ZonedDateTime createdAt;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "organizer_fk")
    private Participant organizer;

    @OneToMany(mappedBy = "event")
    @Singular
    private List<Participant> attendees;
}
