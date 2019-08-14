package ch.surech.chronos.chronosimporter;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChronosImporterApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ChronosImporterApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Los geht's!!");
    }
}
