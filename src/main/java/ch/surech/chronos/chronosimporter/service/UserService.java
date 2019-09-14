package ch.surech.chronos.chronosimporter.service;

import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.models.extensions.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private GraphService graphService;

    public User getUser(){
        User user = graphService.getGraphClient()
                .me()
                .buildRequest()
                .get();
        return user;
    }


    public User getUser(String principalName){
        IGraphServiceClient graphClient = graphService.getGraphClient();

        return graphClient.users(principalName).buildRequest().get();
    }
}
