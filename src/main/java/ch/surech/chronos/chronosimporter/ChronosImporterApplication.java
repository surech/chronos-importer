package ch.surech.chronos.chronosimporter;

import ch.surech.chronos.chronosimporter.service.AuthentificationService;
import ch.surech.chronos.chronosimporter.service.CalendarService;
import ch.surech.chronos.chronosimporter.service.GraphService;
import com.microsoft.graph.models.extensions.Event;
import com.microsoft.graph.models.extensions.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class ChronosImporterApplication implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChronosImporterApplication.class);

    @Autowired
    private AuthentificationService authentificationService;

    @Autowired
    private GraphService graphService;

    @Autowired
    private CalendarService calendarService;

    public static void main(String[] args) {
        SpringApplication.run(ChronosImporterApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        authentificationService.signIn();

        LOGGER.info("Token: " + authentificationService.getAccessToken());

        User user = graphService.getUser();
        LOGGER.info("Welcome {}!", user.displayName);

        List<Event> events = calendarService.getEvents();
        for (Event event : events) {
            System.out.println("Subject: " + event.subject);
            System.out.println("  Organizer: " + event.organizer.emailAddress.name);
            System.out.println("  Start: " + event.start);
            System.out.println("  End: " + event.end);
        }
    }
}
