package ch.surech.chronos.chronosimporter.service;

import ch.surech.chronos.chronosimporter.mapper.ImportedEventMapper;
import com.microsoft.graph.http.GraphServiceException;
import com.microsoft.graph.models.extensions.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImportService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImportService.class);

    @Value("#{'${import.accounts}'.split(',')}")
    private List<String> users;

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private ImportedEventService importedEventService;

    public void runImport(){
        for (String user : users) {
            LOGGER.info("Importing Calendar for User {}", user);
            List<Event> events = null;
            try {
                events = calendarService.getEventsFromCalendar(user);
            } catch (GraphServiceException e) {
                if (e.getServiceError().code.equals("ErrorItemNotFound")) {
                    LOGGER.warn("No calendar for {} found", user);
                    continue;
                }else if(e.getServiceError().code.equals("ErrorAccessDenied")){
                    LOGGER.warn("No access to calender from {}", user);
                    continue;
                } else {
                    throw e;
                }
            }

            events.stream().map(ImportedEventMapper::toModel).forEach(importedEventService::save);
        }
    }
}
