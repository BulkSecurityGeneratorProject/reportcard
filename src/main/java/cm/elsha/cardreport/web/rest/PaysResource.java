package cm.elsha.cardreport.web.rest;

import com.codahale.metrics.annotation.Timed;
import cm.elsha.cardreport.domain.Pays;

import cm.elsha.cardreport.repository.PaysRepository;
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
 * REST controller for managing Pays.
 */
@RestController
@RequestMapping("/api")
public class PaysResource {

    private final Logger log = LoggerFactory.getLogger(PaysResource.class);
        
    @Inject
    private PaysRepository paysRepository;

    /**
     * POST  /pays : Create a new pays.
     *
     * @param pays the pays to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pays, or with status 400 (Bad Request) if the pays has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/pays",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Pays> createPays(@Valid @RequestBody Pays pays) throws URISyntaxException {
        log.debug("REST request to save Pays : {}", pays);
        if (pays.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("pays", "idexists", "A new pays cannot already have an ID")).body(null);
        }
        Pays result = paysRepository.save(pays);
        return ResponseEntity.created(new URI("/api/pays/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("pays", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pays : Updates an existing pays.
     *
     * @param pays the pays to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pays,
     * or with status 400 (Bad Request) if the pays is not valid,
     * or with status 500 (Internal Server Error) if the pays couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/pays",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Pays> updatePays(@Valid @RequestBody Pays pays) throws URISyntaxException {
        log.debug("REST request to update Pays : {}", pays);
        if (pays.getId() == null) {
            return createPays(pays);
        }
        Pays result = paysRepository.save(pays);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("pays", pays.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pays : get all the pays.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pays in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/pays",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Pays>> getAllPays(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Pays");
        Page<Pays> page = paysRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pays");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /pays/:id : get the "id" pays.
     *
     * @param id the id of the pays to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pays, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/pays/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Pays> getPays(@PathVariable Long id) {
        log.debug("REST request to get Pays : {}", id);
        Pays pays = paysRepository.findOne(id);
        return Optional.ofNullable(pays)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /pays/:id : delete the "id" pays.
     *
     * @param id the id of the pays to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/pays/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePays(@PathVariable Long id) {
        log.debug("REST request to delete Pays : {}", id);
        paysRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("pays", id.toString())).build();
    }

}
