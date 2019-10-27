package ch.surech.chronos.chronosimporter.repo;

import ch.surech.chronos.chronosimporter.model.DistinctParticipant;
import ch.surech.chronos.chronosimporter.model.Participant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ParticipantRepository extends CrudRepository<Participant, Long> {

    @Query("SELECT distinct new ch.surech.chronos.chronosimporter.model.DistinctParticipant(p.name, p.address) FROM Participant p")
    List<DistinctParticipant> getDistinctParticipants();

}
