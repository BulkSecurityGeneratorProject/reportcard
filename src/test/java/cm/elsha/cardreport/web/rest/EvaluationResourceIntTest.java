package cm.elsha.cardreport.web.rest;

import cm.elsha.cardreport.ReportcardApp;
import cm.elsha.cardreport.domain.Evaluation;
import cm.elsha.cardreport.repository.EvaluationRepository;

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
 * Test class for the EvaluationResource REST controller.
 *
 * @see EvaluationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReportcardApp.class)
public class EvaluationResourceIntTest {

    private static final Double DEFAULT_NOTE = 0D;
    private static final Double UPDATED_NOTE = 1D;

    private static final Integer DEFAULT_ANNEE = 2016;
    private static final Integer UPDATED_ANNEE = 2017;

    @Inject
    private EvaluationRepository evaluationRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restEvaluationMockMvc;

    private Evaluation evaluation;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EvaluationResource evaluationResource = new EvaluationResource();
        ReflectionTestUtils.setField(evaluationResource, "evaluationRepository", evaluationRepository);
        this.restEvaluationMockMvc = MockMvcBuilders.standaloneSetup(evaluationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Evaluation createEntity(EntityManager em) {
        Evaluation evaluation = new Evaluation();
        evaluation = new Evaluation();
        evaluation.setNote(DEFAULT_NOTE);
        evaluation.setAnnee(DEFAULT_ANNEE);
        return evaluation;
    }

    @Before
    public void initTest() {
        evaluation = createEntity(em);
    }

    @Test
    @Transactional
    public void createEvaluation() throws Exception {
        int databaseSizeBeforeCreate = evaluationRepository.findAll().size();

        // Create the Evaluation

        restEvaluationMockMvc.perform(post("/api/evaluations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(evaluation)))
                .andExpect(status().isCreated());

        // Validate the Evaluation in the database
        List<Evaluation> evaluations = evaluationRepository.findAll();
        assertThat(evaluations).hasSize(databaseSizeBeforeCreate + 1);
        Evaluation testEvaluation = evaluations.get(evaluations.size() - 1);
        assertThat(testEvaluation.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testEvaluation.getAnnee()).isEqualTo(DEFAULT_ANNEE);
    }

    @Test
    @Transactional
    public void checkAnneeIsRequired() throws Exception {
        int databaseSizeBeforeTest = evaluationRepository.findAll().size();
        // set the field null
        evaluation.setAnnee(null);

        // Create the Evaluation, which fails.

        restEvaluationMockMvc.perform(post("/api/evaluations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(evaluation)))
                .andExpect(status().isBadRequest());

        List<Evaluation> evaluations = evaluationRepository.findAll();
        assertThat(evaluations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEvaluations() throws Exception {
        // Initialize the database
        evaluationRepository.saveAndFlush(evaluation);

        // Get all the evaluations
        restEvaluationMockMvc.perform(get("/api/evaluations?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(evaluation.getId().intValue())))
                .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.doubleValue())))
                .andExpect(jsonPath("$.[*].annee").value(hasItem(DEFAULT_ANNEE)));
    }

    @Test
    @Transactional
    public void getEvaluation() throws Exception {
        // Initialize the database
        evaluationRepository.saveAndFlush(evaluation);

        // Get the evaluation
        restEvaluationMockMvc.perform(get("/api/evaluations/{id}", evaluation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(evaluation.getId().intValue()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE.doubleValue()))
            .andExpect(jsonPath("$.annee").value(DEFAULT_ANNEE));
    }

    @Test
    @Transactional
    public void getNonExistingEvaluation() throws Exception {
        // Get the evaluation
        restEvaluationMockMvc.perform(get("/api/evaluations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEvaluation() throws Exception {
        // Initialize the database
        evaluationRepository.saveAndFlush(evaluation);
        int databaseSizeBeforeUpdate = evaluationRepository.findAll().size();

        // Update the evaluation
        Evaluation updatedEvaluation = evaluationRepository.findOne(evaluation.getId());
        updatedEvaluation.setNote(UPDATED_NOTE);
        updatedEvaluation.setAnnee(UPDATED_ANNEE);

        restEvaluationMockMvc.perform(put("/api/evaluations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEvaluation)))
                .andExpect(status().isOk());

        // Validate the Evaluation in the database
        List<Evaluation> evaluations = evaluationRepository.findAll();
        assertThat(evaluations).hasSize(databaseSizeBeforeUpdate);
        Evaluation testEvaluation = evaluations.get(evaluations.size() - 1);
        assertThat(testEvaluation.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testEvaluation.getAnnee()).isEqualTo(UPDATED_ANNEE);
    }

    @Test
    @Transactional
    public void deleteEvaluation() throws Exception {
        // Initialize the database
        evaluationRepository.saveAndFlush(evaluation);
        int databaseSizeBeforeDelete = evaluationRepository.findAll().size();

        // Get the evaluation
        restEvaluationMockMvc.perform(delete("/api/evaluations/{id}", evaluation.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Evaluation> evaluations = evaluationRepository.findAll();
        assertThat(evaluations).hasSize(databaseSizeBeforeDelete - 1);
    }
}
