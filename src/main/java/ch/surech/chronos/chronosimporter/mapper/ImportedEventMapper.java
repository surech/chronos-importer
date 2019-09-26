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
        event.start.

        return builder.build();
    }
}
