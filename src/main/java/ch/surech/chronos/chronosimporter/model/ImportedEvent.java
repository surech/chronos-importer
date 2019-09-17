package ch.surech.chronos.chronosimporter.model;

import com.microsoft.graph.models.generated.Importance;
import com.microsoft.graph.models.generated.LocationType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "importedevent")
public class ImportedEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "icaluid")
    private String iCalUId;

    @Column(name = "subject")
    private String subject;

    @Column(name = "body_preview")
    private String bodyPreview;

    @Column(name = "importance")
    private Importance importance;

    @Column(name = "start", columnDefinition = "TIMESTAMP")
    private LocalDateTime start;

    @Column(name = "end", columnDefinition = "TIMESTAMP")
    private LocalDateTime end;

    @Column(name = "location_name")
    private String locationName;

    @Column(name = "location_uri")
    private String locationUri;

    @Column(name = "location_type")
    private LocationType locationType;

    @Column(name = "location_id")
    private String locationId;

    @Column(name = "is_all_day")
    private boolean isAllDay;
}
