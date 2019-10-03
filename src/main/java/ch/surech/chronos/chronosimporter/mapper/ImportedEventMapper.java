package ch.surech.chronos.chronosimporter.mapper;

import ch.surech.chronos.chronosimporter.model.ImportedEvent;
import com.microsoft.graph.models.extensions.Event;

public class ImportedEventMapper {

    public static ImportedEvent toModel(Event event){
        ImportedEvent.ImportedEventBuilder builder = ImportedEvent.builder();
        builder.iCalUId(event.iCalUId);
        builder.subject(event.subject);
        builder.bodyPreview(event.bodyPreview);
        builder.importance(event.importance);
        builder.start(DateTimeTimeZoneMapper.toZonedDateTime(event.start));
        builder.end(DateTimeTimeZoneMapper.toZonedDateTime(event.end));
        if(event.location != null) {
            builder.locationName(event.location.displayName);
            builder.locationUri(event.location.locationUri);
            builder.locationType(event.location.locationType);
            builder.locationId(event.location.uniqueId);
        }
        builder.isAllDay(event.isAllDay);
        builder.isCancelled(event.isCancelled);
        builder.isOrganizer(event.isOrganizer);
        builder.seriesMasterId(event.seriesMasterId);
        builder.showAs(event.showAs);
        builder.type(event.type);
        builder.organizer(ParticipantMapper.toParticipant(event.organizer));
        event.attendees.stream().map(ParticipantMapper::toParticipant).forEach(builder::attendee);

        return builder.build();
    }
}
