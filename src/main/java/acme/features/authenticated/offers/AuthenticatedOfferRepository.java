
package acme.features.authenticated.offers;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.offer.Offer;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AuthenticatedOfferRepository extends AbstractRepository {

	@Query("select o from Offer o")
	Collection<Offer> findAllOffers();

	@Query("select o from Offer o where o.id =:id")
	Offer findOfferById(int id);

}
