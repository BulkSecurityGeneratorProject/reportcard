package cm.elsha.cardreport.repository;

import cm.elsha.cardreport.domain.AnneeAcademique;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AnneeAcademique entity.
 */
@SuppressWarnings("unused")
public interface AnneeAcademiqueRepository extends JpaRepository<AnneeAcademique,Long> {

}
