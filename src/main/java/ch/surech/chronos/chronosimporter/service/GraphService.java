package ch.surech.chronos.chronosimporter.service;

import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.models.extensions.User;
import com.microsoft.graph.requests.extensions.GraphServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GraphService {

    private IGraphServiceClient graphClient;

    @Autowired
    private AuthentificationService authentificationService;

    public IGraphServiceClient getGraphClient(){
        if(graphClient == null){
            // Create Auth-Provider
            SimpleAuthProvider authProvider = new SimpleAuthProvider(authentificationService.getAccessToken());

            // Build graph client
            this.graphClient = GraphServiceClient.builder().authenticationProvider(authProvider).buildClient();
        }

        return graphClient;
    }

    public User getUser(){
        User user = getGraphClient()
                .me()
                .buildRequest()
                .get();
        return user;
    }
}
