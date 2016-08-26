package cm.elsha.cardreport.web.rest;

import com.codahale.metrics.annotation.Timed;
import cm.elsha.cardreport.domain.Matiere;

import cm.elsha.cardreport.repository.MatiereRepository;
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
 * REST controller for managing Matiere.
 */
@RestController
@RequestMapping("/api")
public class MatiereResource {

    private final Logger log = LoggerFactory.getLogger(MatiereResource.class);
        
    @Inject
    private MatiereRepository matiereRepository;

    /**
     * POST  /matieres : Create a new matiere.
     *
     * @param matiere the matiere to create
     * @return the ResponseEntity with status 201 (Created) and with body the new matiere, or with status 400 (Bad Request) if the matiere has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/matieres",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Matiere> createMatiere(@Valid @RequestBody Matiere matiere) throws URISyntaxException {
        log.debug("REST request to save Matiere : {}", matiere);
        if (matiere.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("matiere", "idexists", "A new matiere cannot already have an ID")).body(null);
        }
        Matiere result = matiereRepository.save(matiere);
        return ResponseEntity.created(new URI("/api/matieres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("matiere", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /matieres : Updates an existing matiere.
     *
     * @param matiere the matiere to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated matiere,
     * or with status 400 (Bad Request) if the matiere is not valid,
     * or with status 500 (Internal Server Error) if the matiere couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/matieres",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Matiere> updateMatiere(@Valid @RequestBody Matiere matiere) throws URISyntaxException {
        log.debug("REST request to update Matiere : {}", matiere);
        if (matiere.getId() == null) {
            return createMatiere(matiere);
        }
        Matiere result = matiereRepository.save(matiere);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("matiere", matiere.getId().toString()))
            .body(result);
    }

    /**
     * GET  /matieres : get all the matieres.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of matieres in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/matieres",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Matiere>> getAllMatieres(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Matieres");
        Page<Matiere> page = matiereRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/matieres");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /matieres/:id : get the "id" matiere.
     *
     * @param id the id of the matiere to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the matiere, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/matieres/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Matiere> getMatiere(@PathVariable Long id) {
        log.debug("REST request to get Matiere : {}", id);
        Matiere matiere = matiereRepository.findOne(id);
        return Optional.ofNullable(matiere)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /matieres/:id : delete the "id" matiere.
     *
     * @param id the id of the matiere to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/matieres/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMatiere(@PathVariable Long id) {
        log.debug("REST request to delete Matiere : {}", id);
        matiereRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("matiere", id.toString())).build();
    }

}
