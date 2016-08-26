package cm.elsha.cardreport.repository;

import cm.elsha.cardreport.domain.Categorie;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Categorie entity.
 */
@SuppressWarnings("unused")
public interface CategorieRepository extends JpaRepository<Categorie,Long> {

}
