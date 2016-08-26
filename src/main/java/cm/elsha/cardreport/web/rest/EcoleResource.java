package cm.elsha.cardreport.web.rest;

import com.codahale.metrics.annotation.Timed;
import cm.elsha.cardreport.domain.Ecole;

import cm.elsha.cardreport.repository.EcoleRepository;
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
 * REST controller for managing Ecole.
 */
@RestController
@RequestMapping("/api")
public class EcoleResource {

    private final Logger log = LoggerFactory.getLogger(EcoleResource.class);
        
    @Inject
    private EcoleRepository ecoleRepository;

    /**
     * POST  /ecoles : Create a new ecole.
     *
     * @param ecole the ecole to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ecole, or with status 400 (Bad Request) if the ecole has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/ecoles",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ecole> createEcole(@Valid @RequestBody Ecole ecole) throws URISyntaxException {
        log.debug("REST request to save Ecole : {}", ecole);
        if (ecole.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("ecole", "idexists", "A new ecole cannot already have an ID")).body(null);
        }
        Ecole result = ecoleRepository.save(ecole);
        return ResponseEntity.created(new URI("/api/ecoles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("ecole", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ecoles : Updates an existing ecole.
     *
     * @param ecole the ecole to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ecole,
     * or with status 400 (Bad Request) if the ecole is not valid,
     * or with status 500 (Internal Server Error) if the ecole couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/ecoles",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ecole> updateEcole(@Valid @RequestBody Ecole ecole) throws URISyntaxException {
        log.debug("REST request to update Ecole : {}", ecole);
        if (ecole.getId() == null) {
            return createEcole(ecole);
        }
        Ecole result = ecoleRepository.save(ecole);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("ecole", ecole.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ecoles : get all the ecoles.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of ecoles in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/ecoles",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Ecole>> getAllEcoles(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Ecoles");
        Page<Ecole> page = ecoleRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ecoles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /ecoles/:id : get the "id" ecole.
     *
     * @param id the id of the ecole to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ecole, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/ecoles/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ecole> getEcole(@PathVariable Long id) {
        log.debug("REST request to get Ecole : {}", id);
        Ecole ecole = ecoleRepository.findOne(id);
        return Optional.ofNullable(ecole)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /ecoles/:id : delete the "id" ecole.
     *
     * @param id the id of the ecole to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/ecoles/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEcole(@PathVariable Long id) {
        log.debug("REST request to delete Ecole : {}", id);
        ecoleRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("ecole", id.toString())).build();
    }

}
