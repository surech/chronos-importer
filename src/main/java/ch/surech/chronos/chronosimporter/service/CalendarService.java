package ch.surech.chronos.chronosimporter.service;

import com.microsoft.graph.models.extensions.Event;
import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.options.Option;
import com.microsoft.graph.options.QueryOption;
import com.microsoft.graph.requests.extensions.IEventCollectionPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class CalendarService {

    @Autowired
    private GraphService graphService;

    public List<Event> getEvents(String userId){
        IGraphServiceClient graphClient = graphService.getGraphClient();

        // Use QueryOption to specify the $orderby query parameter
        final List<Option> options = new LinkedList<Option>();
        // Sort results by createdDateTime, get newest first
        options.add(new QueryOption("orderby", "createdDateTime DESC"));

        // GET /me/events
        IEventCollectionPage eventPage = graphClient
                .users(userId)
                .events()
                .buildRequest(options)
//                .select("subject,organizer,start,end")
                .get();

        return eventPage.getCurrentPage();
    }

}
