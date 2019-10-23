package ch.surech.chronos.chronosimporter.service;

import ch.surech.chronos.chronosimporter.model.DistinctParticipant;
import ch.surech.chronos.chronosimporter.repo.ParticipantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CreateUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateUserService.class);

    @Autowired
    private ParticipantRepository participantRepository;

    public void createUsers() {
        // Load users
        Set<String> addresses = new HashSet<>();
        List<DistinctParticipant> distinctParticipants = participantRepository.getDistinctParticipants();
        distinctParticipants.stream()
                .filter(this::filterUser)
                .filter(p -> addresses.add(p.getAddress()))
                .map(p -> p.getName() + "(" + p.getAddress() + ")")
                .forEach(LOGGER::info);
    }

    private boolean filterUser(DistinctParticipant distinctParticipant) {
        String address = distinctParticipant.getAddress();

        // Ignore Users with no address
        if (address == null || address.isBlank()) {
            return false;
        }

        // Only sbb.ch-Users allowed
        if (!address.endsWith("sbb.ch")) {
            LOGGER.debug("Ignoring {} because it's not an sbb.ch-address", address);
            return false;
        }

        String pattern = "[a-zA-Z]+\\d+@.+";
        if(address.matches(pattern)) {
            LOGGER.debug("Ignoring {} because it's a systemuser", address);
            return false;
        }
        return true;
    }

}
