package cm.elsha.cardreport.repository;

import cm.elsha.cardreport.domain.Inscrire;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Inscrire entity.
 */
@SuppressWarnings("unused")
public interface InscrireRepository extends JpaRepository<Inscrire,Long> {

}
