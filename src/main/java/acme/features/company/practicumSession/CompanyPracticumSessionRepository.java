
package acme.features.company.practicumSession;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.practicum.Practicum;
import acme.entities.practicumSessions.PracticumSession;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface CompanyPracticumSessionRepository extends AbstractRepository {

	@Query("SELECT p FROM PracticumSession p WHERE p.practicum.id = :id")
	Collection<PracticumSession> findPracticumSessionsByPracticumId(int id);

	@Query("SELECT p FROM PracticumSession p WHERE p.id = :id")
	PracticumSession findPracticumSessionById(int id);

	@Query("SELECT p FROM Practicum p WHERE p.id = :id")
	Practicum findPracticumById(int id);

	@Query("SELECT p FROM PracticumSession p WHERE p.addendum = :bool AND p.practicum.id = :id")
	Optional<PracticumSession> findAddendum(boolean bool, int id);
}
