package ch.surech.chronos.chronosimporter.service;

import com.microsoft.graph.models.extensions.Group;
import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.options.Option;
import com.microsoft.graph.options.QueryOption;
import com.microsoft.graph.requests.extensions.IGroupCollectionPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class GroupService {
    @Autowired
    private GraphService graphService;

    public List<Group> searchGroupByDisplayName(String displayName){
        IGraphServiceClient graphClient = graphService.getGraphClient();

        final List<Option> options = new LinkedList<Option>();
        options.add(new QueryOption("$filter", "displayName eq 'DL TIMO-Factory'"));

        IGroupCollectionPage groups = graphClient
                .groups()
                .buildRequest(options)
                .get();

        return groups.getCurrentPage();

    }
}
