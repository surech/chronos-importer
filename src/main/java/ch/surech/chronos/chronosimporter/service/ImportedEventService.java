package ch.surech.chronos.chronosimporter.service;

import ch.surech.chronos.chronosimporter.model.ImportedEvent;
import ch.surech.chronos.chronosimporter.model.Participant;
import ch.surech.chronos.chronosimporter.repo.ParticipantRepository;
import ch.surech.chronos.chronosimporter.repo.ImportedEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ImportedEventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportedEventService.class);

    @Autowired
    private ImportedEventRepository importedEventRepository;

    private final Set<String> existingEventIds = new HashSet<>();

    @PostConstruct
    private void init(){
        // Load all IDs of the existing events
        List<String> ids = importedEventRepository.getAllICalUIds();
        existingEventIds.addAll(ids);
    }

    @Autowired
    private ParticipantRepository participantRepository;

    public ImportedEvent save(ImportedEvent event){
        // Überprüfen ob das Event schon existiert
        if (existingEventIds.contains(event.getICalUId())) {
            LOGGER.info("Event with ID {} allready exists and will not stored again", event.getICalUId());
            return event;
        }

        // Event speichern
        ImportedEvent savedEvent = importedEventRepository.save(event);

        // Teilnehmer speichern
        if(event.getAttendees() != null){
            for (Participant attendee : event.getAttendees()) {
                attendee.setEvent(savedEvent);
                participantRepository.save(attendee);
            }
        }

        existingEventIds.add(event.getICalUId());
        return savedEvent;

    }
}
