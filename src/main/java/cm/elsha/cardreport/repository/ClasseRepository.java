package cm.elsha.cardreport.repository;

import cm.elsha.cardreport.domain.Classe;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Classe entity.
 */
@SuppressWarnings("unused")
public interface ClasseRepository extends JpaRepository<Classe,Long> {

}
