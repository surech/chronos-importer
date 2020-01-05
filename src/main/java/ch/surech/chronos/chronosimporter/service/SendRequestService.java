package ch.surech.chronos.chronosimporter.service;

import ch.surech.chronos.api.model.Event;
import ch.surech.chronos.api.model.Invitee;
import ch.surech.chronos.api.model.MeetingRequest;
import ch.surech.chronos.chronosimporter.model.ImportedEvent;
import ch.surech.chronos.chronosimporter.model.Participant;
import ch.surech.chronos.chronosimporter.repo.ImportedEventRepository;
import com.microsoft.graph.models.generated.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SendRequestService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendRequestService.class);

    @Value("${chronos.server}")
    private String chronosServer;

    @Autowired
    private ImportedEventRepository importedEventRepository;

    public void sendRequests(){
        // Load all Events
//        List<ImportedEvent> events = importedEventRepository.getAll();
        List<ImportedEvent> events = importedEventRepository.getAllForExport();

        int count = 0;
        for (ImportedEvent event : events) {
            LOGGER.info("Creating {} of {} Meetings", ++count, events.size());
            if (!filterEvent(event)) {
                LOGGER.info("Ignoring Event {}", event.getSubject());
                continue;
            }

            sendRequest(event);
        }
    }

    private boolean filterEvent(ImportedEvent event) {
        // Ignore Events with only one attendee
        if (event.getAttendees().size() < 2) {
            return false;
        }

        // Ignore Events with too many attendees
        if (event.getAttendees().size() > 10) {
            return false;
        }

        // Check duration
        Duration duration = Duration.between(event.getStart(), event.getEnd());
        long minutes = duration.toMinutes();
        if (minutes < 1 || minutes % 15 != 0 || minutes > 420) {
            return false;
        }

        // Ignore Canceled events
        if (event.isCancelled()) {
            return false;
        }

        // We're only looking for single events
        if (event.getType() != EventType.SINGLE_INSTANCE) {
            return false;
        }

        return true;
    }

    public void sendRequest(ImportedEvent event) {
        // Time-Range is from tomorrow within the next three weeks
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalTime startTime = LocalTime.MIDNIGHT;
        LocalDate endDate = startDate.plusWeeks(3);
        Duration duration = Duration.between(event.getStart(), event.getEnd());

        List<Invitee> invitees = event.getAttendees().stream()
                .map(Participant::getAddress)
                .map(e -> Invitee.builder().email(e).optional(false).build())
                .collect(Collectors.toList());

        // Create MeetingRequest
        MeetingRequest.MeetingRequestBuilder builder = MeetingRequest.builder();
        builder.organizer(event.getOrganizer().getAddress());
        builder.startRange(ZonedDateTime.of(startDate, startTime, ZoneId.systemDefault()));
        builder.endRange(ZonedDateTime.of(endDate, startTime, ZoneId.systemDefault()));
        builder.durationInMinutes((int) duration.toMinutes());
        builder.invitees(invitees);
        builder.subject(event.getSubject());
        builder.body(event.getBodyPreview());
        MeetingRequest meetingRequest = builder.build();

        // Send Request
        try {
            sendRequest(meetingRequest);
        } catch (Exception e) {
            LOGGER.warn("Could not save event", e);
        }
    }

    private void sendRequest(MeetingRequest meetingRequest) {
        RestTemplate restTemplate = new RestTemplate();
        String requestUrl = chronosServer + "/request";
        ResponseEntity<Event> response = restTemplate.postForEntity(requestUrl, meetingRequest, Event.class);

        Event event = response.getBody();
        LOGGER.info("Meeting created at {}", event.getStart().toLocalDateTime());
    }
}
