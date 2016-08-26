package cm.elsha.cardreport.repository;

import cm.elsha.cardreport.domain.Ecole;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Ecole entity.
 */
@SuppressWarnings("unused")
public interface EcoleRepository extends JpaRepository<Ecole,Long> {

}
