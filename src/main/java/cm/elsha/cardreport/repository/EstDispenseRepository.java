package cm.elsha.cardreport.repository;

import cm.elsha.cardreport.domain.EstDispense;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the EstDispense entity.
 */
@SuppressWarnings("unused")
public interface EstDispenseRepository extends JpaRepository<EstDispense,Long> {

    @Query("select estDispense from EstDispense estDispense where estDispense.user.login = ?#{principal.username}")
    List<EstDispense> findByUserIsCurrentUser();

}
