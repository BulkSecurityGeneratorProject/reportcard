package cm.elsha.cardreport.web.rest;

import com.codahale.metrics.annotation.Timed;
import cm.elsha.cardreport.domain.Inscrire;

import cm.elsha.cardreport.repository.InscrireRepository;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Inscrire.
 */
@RestController
@RequestMapping("/api")
public class InscrireResource {

    private final Logger log = LoggerFactory.getLogger(InscrireResource.class);
        
    @Inject
    private InscrireRepository inscrireRepository;

    /**
     * POST  /inscrires : Create a new inscrire.
     *
     * @param inscrire the inscrire to create
     * @return the ResponseEntity with status 201 (Created) and with body the new inscrire, or with status 400 (Bad Request) if the inscrire has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/inscrires",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Inscrire> createInscrire(@RequestBody Inscrire inscrire) throws URISyntaxException {
        log.debug("REST request to save Inscrire : {}", inscrire);
        if (inscrire.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("inscrire", "idexists", "A new inscrire cannot already have an ID")).body(null);
        }
        Inscrire result = inscrireRepository.save(inscrire);
        return ResponseEntity.created(new URI("/api/inscrires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("inscrire", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /inscrires : Updates an existing inscrire.
     *
     * @param inscrire the inscrire to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated inscrire,
     * or with status 400 (Bad Request) if the inscrire is not valid,
     * or with status 500 (Internal Server Error) if the inscrire couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/inscrires",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Inscrire> updateInscrire(@RequestBody Inscrire inscrire) throws URISyntaxException {
        log.debug("REST request to update Inscrire : {}", inscrire);
        if (inscrire.getId() == null) {
            return createInscrire(inscrire);
        }
        Inscrire result = inscrireRepository.save(inscrire);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("inscrire", inscrire.getId().toString()))
            .body(result);
    }

    /**
     * GET  /inscrires : get all the inscrires.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of inscrires in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/inscrires",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Inscrire>> getAllInscrires(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Inscrires");
        Page<Inscrire> page = inscrireRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/inscrires");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /inscrires/:id : get the "id" inscrire.
     *
     * @param id the id of the inscrire to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the inscrire, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/inscrires/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Inscrire> getInscrire(@PathVariable Long id) {
        log.debug("REST request to get Inscrire : {}", id);
        Inscrire inscrire = inscrireRepository.findOne(id);
        return Optional.ofNullable(inscrire)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /inscrires/:id : delete the "id" inscrire.
     *
     * @param id the id of the inscrire to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/inscrires/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInscrire(@PathVariable Long id) {
        log.debug("REST request to delete Inscrire : {}", id);
        inscrireRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("inscrire", id.toString())).build();
    }

}
