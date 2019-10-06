package ch.surech.chronos.chronosimporter.repo;

import ch.surech.chronos.chronosimporter.model.ImportedEvent;
import org.springframework.data.repository.CrudRepository;

public interface ImportedEventRepository extends CrudRepository<ImportedEvent, Long> {
}
