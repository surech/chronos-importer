package ch.surech.chronos.chronosimporter.service;

import ch.surech.chronos.api.model.User;
import ch.surech.chronos.api.model.UserPrecentePreference;
import ch.surech.chronos.api.model.Weekdays;
import ch.surech.chronos.chronosimporter.model.DistinctParticipant;
import ch.surech.chronos.chronosimporter.model.PrecentePreferenceStereotype;
import ch.surech.chronos.chronosimporter.repo.ParticipantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class CreateUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateUserService.class);

    @Value("${chronos.server}")
    private String chronosServer;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private PrecentePreferenceFactoryService precentePreferenceFactoryService;

    public void createUsers() {
        // Load users
        Set<String> addresses = new HashSet<>();
        List<DistinctParticipant> distinctParticipants = participantRepository.getDistinctParticipants();

        distinctParticipants.stream()
                .filter(this::filterUser)
                .filter(p -> addresses.add(p.getAddress()))
                .map(this::createUser)
                .map(this::saveUser)
                .forEach(this::createPrecentePreference);
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

    private User createUser(DistinctParticipant participant) {
        User.UserBuilder builder = User.builder();
        builder.email(participant.getAddress());
        builder.name(participant.getName());

        // Everyone is working througout the week
        EnumSet<Weekdays> workingdays = EnumSet.of(Weekdays.Monday, Weekdays.Tuesday, Weekdays.Wednesday, Weekdays.Thursday, Weekdays.Friday);

        // But 1 of 5 is working 80%, either on Monday or Friday
        int rnd = ThreadLocalRandom.current().nextInt(10);
        if(rnd == 0) {
            workingdays.remove(Weekdays.Monday);
        } else if(rnd == 1){
            workingdays.remove(Weekdays.Friday);
        }

        builder.workingDays(workingdays);
        return builder.build();
    }

    private User saveUser(User user){
        RestTemplate restTemplate = new RestTemplate();
        String userUrl = chronosServer + "/user";
        ResponseEntity<User> response = restTemplate.postForEntity(userUrl, user, User.class);
        return response.getBody();
    }

    private List<UserPrecentePreference> createPrecentePreference(User user){
        // Create Preference
        List<UserPrecentePreference> userPrecentePreferences = null;
        if ("stefan.urech@sbb.ch".equals(user.getEmail())) {
            userPrecentePreferences = this.precentePreferenceFactoryService.buildPrecente(PrecentePreferenceStereotype.SMART_WORKER);
        } else {
            userPrecentePreferences = this.precentePreferenceFactoryService.buildRandomPrecence();
        }

        List<UserPrecentePreference> result = userPrecentePreferences.stream().map(pp -> savePrecentePreference(user, pp)).collect(Collectors.toList());
        return result;
    }

    private UserPrecentePreference savePrecentePreference(User user, UserPrecentePreference pp) {
        RestTemplate restTemplate = new RestTemplate();
        String userUrl = chronosServer + "/user/" + user.getEmail() + "/precencePreference";
        ResponseEntity<UserPrecentePreference> response = restTemplate.postForEntity(userUrl, pp, UserPrecentePreference.class);
        return response.getBody();

    }

}
