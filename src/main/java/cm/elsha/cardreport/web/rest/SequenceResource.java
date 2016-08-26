package cm.elsha.cardreport.web.rest;

import com.codahale.metrics.annotation.Timed;
import cm.elsha.cardreport.domain.Sequence;

import cm.elsha.cardreport.repository.SequenceRepository;
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
 * REST controller for managing Sequence.
 */
@RestController
@RequestMapping("/api")
public class SequenceResource {

    private final Logger log = LoggerFactory.getLogger(SequenceResource.class);
        
    @Inject
    private SequenceRepository sequenceRepository;

    /**
     * POST  /sequences : Create a new sequence.
     *
     * @param sequence the sequence to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sequence, or with status 400 (Bad Request) if the sequence has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/sequences",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Sequence> createSequence(@Valid @RequestBody Sequence sequence) throws URISyntaxException {
        log.debug("REST request to save Sequence : {}", sequence);
        if (sequence.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("sequence", "idexists", "A new sequence cannot already have an ID")).body(null);
        }
        Sequence result = sequenceRepository.save(sequence);
        return ResponseEntity.created(new URI("/api/sequences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("sequence", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sequences : Updates an existing sequence.
     *
     * @param sequence the sequence to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sequence,
     * or with status 400 (Bad Request) if the sequence is not valid,
     * or with status 500 (Internal Server Error) if the sequence couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/sequences",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Sequence> updateSequence(@Valid @RequestBody Sequence sequence) throws URISyntaxException {
        log.debug("REST request to update Sequence : {}", sequence);
        if (sequence.getId() == null) {
            return createSequence(sequence);
        }
        Sequence result = sequenceRepository.save(sequence);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("sequence", sequence.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sequences : get all the sequences.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of sequences in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/sequences",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Sequence>> getAllSequences(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Sequences");
        Page<Sequence> page = sequenceRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sequences");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sequences/:id : get the "id" sequence.
     *
     * @param id the id of the sequence to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sequence, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/sequences/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Sequence> getSequence(@PathVariable Long id) {
        log.debug("REST request to get Sequence : {}", id);
        Sequence sequence = sequenceRepository.findOne(id);
        return Optional.ofNullable(sequence)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /sequences/:id : delete the "id" sequence.
     *
     * @param id the id of the sequence to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/sequences/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSequence(@PathVariable Long id) {
        log.debug("REST request to delete Sequence : {}", id);
        sequenceRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("sequence", id.toString())).build();
    }

}
