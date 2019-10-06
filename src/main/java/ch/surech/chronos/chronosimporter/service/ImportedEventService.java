package ch.surech.chronos.chronosimporter.service;

import ch.surech.chronos.chronosimporter.model.ImportedEvent;
import ch.surech.chronos.chronosimporter.model.Participant;
import ch.surech.chronos.chronosimporter.model.ParticipantRepository;
import ch.surech.chronos.chronosimporter.repo.ImportedEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImportedEventService {
    @Autowired
    private ImportedEventRepository importedEventRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    public ImportedEvent save(ImportedEvent event){
        // Event speichern
        ImportedEvent savedEvent = importedEventRepository.save(event);

        // Teilnehmer speichern
        if(event.getAttendees() != null){
            for (Participant attendee : event.getAttendees()) {
                attendee.setEvent(savedEvent);
                participantRepository.save(attendee);
            }
        }

        return savedEvent;

    }
}
