package ch.surech.chronos.chronosimporter.service;

import ch.surech.chronos.chronosimporter.model.DistinctParticipant;
import ch.surech.chronos.chronosimporter.repo.ParticipantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreateUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateUserService.class);

    @Autowired
    private ParticipantRepository participantRepository;

    public void createUsers(){
        // Load users
        List<DistinctParticipant> distinctParticipants = participantRepository.getDistinctParticipants();
        distinctParticipants.stream().map(p -> p.getName()).forEach(LOGGER::info);
    }
}
