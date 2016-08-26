package cm.elsha.cardreport.repository;

import cm.elsha.cardreport.domain.Eleve;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Eleve entity.
 */
@SuppressWarnings("unused")
public interface EleveRepository extends JpaRepository<Eleve,Long> {

}
