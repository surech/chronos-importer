package ch.surech.chronos.chronosimporter;

import ch.surech.chronos.chronosimporter.service.AuthentificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChronosImporterApplication implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChronosImporterApplication.class);

    @Autowired
    private AuthentificationService authentificationService;

    public static void main(String[] args) {
        SpringApplication.run(ChronosImporterApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        authentificationService.signIn();

        LOGGER.info("Token: " + authentificationService.getAccessToken());
    }
}
