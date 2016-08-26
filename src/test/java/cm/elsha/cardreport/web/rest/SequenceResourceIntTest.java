package cm.elsha.cardreport.web.rest;

import cm.elsha.cardreport.ReportcardApp;
import cm.elsha.cardreport.domain.Sequence;
import cm.elsha.cardreport.repository.SequenceRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SequenceResource REST controller.
 *
 * @see SequenceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReportcardApp.class)
public class SequenceResourceIntTest {

    private static final Integer DEFAULT_NOM = 1;
    private static final Integer UPDATED_NOM = 2;

    @Inject
    private SequenceRepository sequenceRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restSequenceMockMvc;

    private Sequence sequence;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SequenceResource sequenceResource = new SequenceResource();
        ReflectionTestUtils.setField(sequenceResource, "sequenceRepository", sequenceRepository);
        this.restSequenceMockMvc = MockMvcBuilders.standaloneSetup(sequenceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sequence createEntity(EntityManager em) {
        Sequence sequence = new Sequence();
        sequence = new Sequence();
        sequence.setNom(DEFAULT_NOM);
        return sequence;
    }

    @Before
    public void initTest() {
        sequence = createEntity(em);
    }

    @Test
    @Transactional
    public void createSequence() throws Exception {
        int databaseSizeBeforeCreate = sequenceRepository.findAll().size();

        // Create the Sequence

        restSequenceMockMvc.perform(post("/api/sequences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sequence)))
                .andExpect(status().isCreated());

        // Validate the Sequence in the database
        List<Sequence> sequences = sequenceRepository.findAll();
        assertThat(sequences).hasSize(databaseSizeBeforeCreate + 1);
        Sequence testSequence = sequences.get(sequences.size() - 1);
        assertThat(testSequence.getNom()).isEqualTo(DEFAULT_NOM);
    }

    @Test
    @Transactional
    public void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = sequenceRepository.findAll().size();
        // set the field null
        sequence.setNom(null);

        // Create the Sequence, which fails.

        restSequenceMockMvc.perform(post("/api/sequences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sequence)))
                .andExpect(status().isBadRequest());

        List<Sequence> sequences = sequenceRepository.findAll();
        assertThat(sequences).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSequences() throws Exception {
        // Initialize the database
        sequenceRepository.saveAndFlush(sequence);

        // Get all the sequences
        restSequenceMockMvc.perform(get("/api/sequences?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(sequence.getId().intValue())))
                .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)));
    }

    @Test
    @Transactional
    public void getSequence() throws Exception {
        // Initialize the database
        sequenceRepository.saveAndFlush(sequence);

        // Get the sequence
        restSequenceMockMvc.perform(get("/api/sequences/{id}", sequence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sequence.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM));
    }

    @Test
    @Transactional
    public void getNonExistingSequence() throws Exception {
        // Get the sequence
        restSequenceMockMvc.perform(get("/api/sequences/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSequence() throws Exception {
        // Initialize the database
        sequenceRepository.saveAndFlush(sequence);
        int databaseSizeBeforeUpdate = sequenceRepository.findAll().size();

        // Update the sequence
        Sequence updatedSequence = sequenceRepository.findOne(sequence.getId());
        updatedSequence.setNom(UPDATED_NOM);

        restSequenceMockMvc.perform(put("/api/sequences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSequence)))
                .andExpect(status().isOk());

        // Validate the Sequence in the database
        List<Sequence> sequences = sequenceRepository.findAll();
        assertThat(sequences).hasSize(databaseSizeBeforeUpdate);
        Sequence testSequence = sequences.get(sequences.size() - 1);
        assertThat(testSequence.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    public void deleteSequence() throws Exception {
        // Initialize the database
        sequenceRepository.saveAndFlush(sequence);
        int databaseSizeBeforeDelete = sequenceRepository.findAll().size();

        // Get the sequence
        restSequenceMockMvc.perform(delete("/api/sequences/{id}", sequence.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Sequence> sequences = sequenceRepository.findAll();
        assertThat(sequences).hasSize(databaseSizeBeforeDelete - 1);
    }
}
