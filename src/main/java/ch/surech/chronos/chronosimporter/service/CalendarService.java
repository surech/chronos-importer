package ch.surech.chronos.chronosimporter.service;

import com.microsoft.graph.models.extensions.Calendar;
import com.microsoft.graph.models.extensions.Event;
import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.options.HeaderOption;
import com.microsoft.graph.options.Option;
import com.microsoft.graph.options.QueryOption;
import com.microsoft.graph.requests.extensions.IEventCollectionPage;
import com.microsoft.graph.requests.extensions.IEventCollectionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class CalendarService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CalendarService.class);

    @Autowired
    private GraphService graphService;

    public List<Event> getEvents(String userId){
        IGraphServiceClient graphClient = graphService.getGraphClient();

        // Use QueryOption to specify the $orderby query parameter
        final List<Option> options = new LinkedList<Option>();
        // Sort results by createdDateTime, get newest first
        options.add(new QueryOption("orderby", "createdDateTime DESC"));
        options.add(new HeaderOption("Prefer", "outlook.timezone=\"Europe/Paris\""));

        // GET /me/events
        IEventCollectionPage eventPage = graphClient
                .users(userId)
                .events()
                .buildRequest(options)
//                .select("subject,organizer,start,end")
                .get();

        return eventPage.getCurrentPage();
    }

    public List<Event> getEventsFromCalendar(String userId){
        IGraphServiceClient graphClient = graphService.getGraphClient();

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusWeeks(3);

        String startString = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(start);
        String endString = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(end);

        List<Event> result = new ArrayList<>();

        final List<Option> options = new LinkedList<Option>();
        options.add(new QueryOption("startDateTime", startString));
        options.add(new QueryOption("endDateTime", endString));
        options.add(new QueryOption("top", "100"));

        IEventCollectionRequest request = graphClient
                .users(userId)
                .calendar()
                .calendarView()
                .buildRequest(options);

        loadAllEvents(request, result);
        return result;
    }

    private void loadAllEvents(IEventCollectionRequest buildRequest, List<Event> result) {
        IEventCollectionPage events = buildRequest.get();
        LOGGER.info("Loaded {} Events...", events.getCurrentPage().size());

        result.addAll(events.getCurrentPage());

        if(events.getNextPage() != null){

            loadAllEvents(events.getNextPage().buildRequest(), result);
        }
    }

}
