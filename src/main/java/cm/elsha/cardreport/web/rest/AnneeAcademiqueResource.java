package cm.elsha.cardreport.web.rest;

import com.codahale.metrics.annotation.Timed;
import cm.elsha.cardreport.domain.AnneeAcademique;

import cm.elsha.cardreport.repository.AnneeAcademiqueRepository;
import cm.elsha.cardreport.web.rest.util.HeaderUtil;
import cm.elsha.cardreport.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing AnneeAcademique.
 */
@RestController
@RequestMapping("/api")
public class AnneeAcademiqueResource {

    private final Logger log = LoggerFactory.getLogger(AnneeAcademiqueResource.class);
        
    @Inject
    private AnneeAcademiqueRepository anneeAcademiqueRepository;

    /**
     * POST  /annee-academiques : Create a new anneeAcademique.
     *
     * @param anneeAcademique the anneeAcademique to create
     * @return the ResponseEntity with status 201 (Created) and with body the new anneeAcademique, or with status 400 (Bad Request) if the anneeAcademique has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/annee-academiques",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AnneeAcademique> createAnneeAcademique(@Valid @RequestBody AnneeAcademique anneeAcademique) throws URISyntaxException {
        log.debug("REST request to save AnneeAcademique : {}", anneeAcademique);
        if (anneeAcademique.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("anneeAcademique", "idexists", "A new anneeAcademique cannot already have an ID")).body(null);
        }
        AnneeAcademique result = anneeAcademiqueRepository.save(anneeAcademique);
        return ResponseEntity.created(new URI("/api/annee-academiques/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("anneeAcademique", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /annee-academiques : Updates an existing anneeAcademique.
     *
     * @param anneeAcademique the anneeAcademique to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated anneeAcademique,
     * or with status 400 (Bad Request) if the anneeAcademique is not valid,
     * or with status 500 (Internal Server Error) if the anneeAcademique couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/annee-academiques",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AnneeAcademique> updateAnneeAcademique(@Valid @RequestBody AnneeAcademique anneeAcademique) throws URISyntaxException {
        log.debug("REST request to update AnneeAcademique : {}", anneeAcademique);
        if (anneeAcademique.getId() == null) {
            return createAnneeAcademique(anneeAcademique);
        }
        AnneeAcademique result = anneeAcademiqueRepository.save(anneeAcademique);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("anneeAcademique", anneeAcademique.getId().toString()))
            .body(result);
    }

    /**
     * GET  /annee-academiques : get all the anneeAcademiques.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of anneeAcademiques in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/annee-academiques",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<AnneeAcademique>> getAllAnneeAcademiques(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of AnneeAcademiques");
        Page<AnneeAcademique> page = anneeAcademiqueRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/annee-academiques");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /annee-academiques/:id : get the "id" anneeAcademique.
     *
     * @param id the id of the anneeAcademique to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the anneeAcademique, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/annee-academiques/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AnneeAcademique> getAnneeAcademique(@PathVariable Long id) {
        log.debug("REST request to get AnneeAcademique : {}", id);
        AnneeAcademique anneeAcademique = anneeAcademiqueRepository.findOne(id);
        return Optional.ofNullable(anneeAcademique)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /annee-academiques/:id : delete the "id" anneeAcademique.
     *
     * @param id the id of the anneeAcademique to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/annee-academiques/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAnneeAcademique(@PathVariable Long id) {
        log.debug("REST request to delete AnneeAcademique : {}", id);
        anneeAcademiqueRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("anneeAcademique", id.toString())).build();
    }

}
