package cm.elsha.cardreport.web.rest;

import com.codahale.metrics.annotation.Timed;
import cm.elsha.cardreport.domain.EstDispense;

import cm.elsha.cardreport.repository.EstDispenseRepository;
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
 * REST controller for managing EstDispense.
 */
@RestController
@RequestMapping("/api")
public class EstDispenseResource {

    private final Logger log = LoggerFactory.getLogger(EstDispenseResource.class);
        
    @Inject
    private EstDispenseRepository estDispenseRepository;

    /**
     * POST  /est-dispenses : Create a new estDispense.
     *
     * @param estDispense the estDispense to create
     * @return the ResponseEntity with status 201 (Created) and with body the new estDispense, or with status 400 (Bad Request) if the estDispense has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/est-dispenses",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EstDispense> createEstDispense(@Valid @RequestBody EstDispense estDispense) throws URISyntaxException {
        log.debug("REST request to save EstDispense : {}", estDispense);
        if (estDispense.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("estDispense", "idexists", "A new estDispense cannot already have an ID")).body(null);
        }
        EstDispense result = estDispenseRepository.save(estDispense);
        return ResponseEntity.created(new URI("/api/est-dispenses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("estDispense", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /est-dispenses : Updates an existing estDispense.
     *
     * @param estDispense the estDispense to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated estDispense,
     * or with status 400 (Bad Request) if the estDispense is not valid,
     * or with status 500 (Internal Server Error) if the estDispense couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/est-dispenses",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EstDispense> updateEstDispense(@Valid @RequestBody EstDispense estDispense) throws URISyntaxException {
        log.debug("REST request to update EstDispense : {}", estDispense);
        if (estDispense.getId() == null) {
            return createEstDispense(estDispense);
        }
        EstDispense result = estDispenseRepository.save(estDispense);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("estDispense", estDispense.getId().toString()))
            .body(result);
    }

    /**
     * GET  /est-dispenses : get all the estDispenses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of estDispenses in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/est-dispenses",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<EstDispense>> getAllEstDispenses(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of EstDispenses");
        Page<EstDispense> page = estDispenseRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/est-dispenses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /est-dispenses/:id : get the "id" estDispense.
     *
     * @param id the id of the estDispense to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the estDispense, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/est-dispenses/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EstDispense> getEstDispense(@PathVariable Long id) {
        log.debug("REST request to get EstDispense : {}", id);
        EstDispense estDispense = estDispenseRepository.findOne(id);
        return Optional.ofNullable(estDispense)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /est-dispenses/:id : delete the "id" estDispense.
     *
     * @param id the id of the estDispense to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/est-dispenses/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEstDispense(@PathVariable Long id) {
        log.debug("REST request to delete EstDispense : {}", id);
        estDispenseRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("estDispense", id.toString())).build();
    }

}
