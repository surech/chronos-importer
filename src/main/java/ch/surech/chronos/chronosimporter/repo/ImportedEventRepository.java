package ch.surech.chronos.chronosimporter.repo;

import ch.surech.chronos.chronosimporter.model.ImportedEvent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ImportedEventRepository extends CrudRepository<ImportedEvent, Long> {

    @Query("SELECT e.iCalUId FROM ImportedEvent e")
    List<String> getAllICalUIds();
}
