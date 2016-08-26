package cm.elsha.cardreport.web.rest;

import cm.elsha.cardreport.ReportcardApp;
import cm.elsha.cardreport.domain.EstDispense;
import cm.elsha.cardreport.repository.EstDispenseRepository;

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
 * Test class for the EstDispenseResource REST controller.
 *
 * @see EstDispenseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReportcardApp.class)
public class EstDispenseResourceIntTest {

    private static final Integer DEFAULT_ANNEE = 2016;
    private static final Integer UPDATED_ANNEE = 2017;

    private static final Integer DEFAULT_COEFFICIENT = 1;
    private static final Integer UPDATED_COEFFICIENT = 2;

    @Inject
    private EstDispenseRepository estDispenseRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restEstDispenseMockMvc;

    private EstDispense estDispense;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EstDispenseResource estDispenseResource = new EstDispenseResource();
        ReflectionTestUtils.setField(estDispenseResource, "estDispenseRepository", estDispenseRepository);
        this.restEstDispenseMockMvc = MockMvcBuilders.standaloneSetup(estDispenseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EstDispense createEntity(EntityManager em) {
        EstDispense estDispense = new EstDispense();
        estDispense = new EstDispense();
        estDispense.setAnnee(DEFAULT_ANNEE);
        estDispense.setCoefficient(DEFAULT_COEFFICIENT);
        return estDispense;
    }

    @Before
    public void initTest() {
        estDispense = createEntity(em);
    }

    @Test
    @Transactional
    public void createEstDispense() throws Exception {
        int databaseSizeBeforeCreate = estDispenseRepository.findAll().size();

        // Create the EstDispense

        restEstDispenseMockMvc.perform(post("/api/est-dispenses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(estDispense)))
                .andExpect(status().isCreated());

        // Validate the EstDispense in the database
        List<EstDispense> estDispenses = estDispenseRepository.findAll();
        assertThat(estDispenses).hasSize(databaseSizeBeforeCreate + 1);
        EstDispense testEstDispense = estDispenses.get(estDispenses.size() - 1);
        assertThat(testEstDispense.getAnnee()).isEqualTo(DEFAULT_ANNEE);
        assertThat(testEstDispense.getCoefficient()).isEqualTo(DEFAULT_COEFFICIENT);
    }

    @Test
    @Transactional
    public void checkCoefficientIsRequired() throws Exception {
        int databaseSizeBeforeTest = estDispenseRepository.findAll().size();
        // set the field null
        estDispense.setCoefficient(null);

        // Create the EstDispense, which fails.

        restEstDispenseMockMvc.perform(post("/api/est-dispenses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(estDispense)))
                .andExpect(status().isBadRequest());

        List<EstDispense> estDispenses = estDispenseRepository.findAll();
        assertThat(estDispenses).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEstDispenses() throws Exception {
        // Initialize the database
        estDispenseRepository.saveAndFlush(estDispense);

        // Get all the estDispenses
        restEstDispenseMockMvc.perform(get("/api/est-dispenses?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(estDispense.getId().intValue())))
                .andExpect(jsonPath("$.[*].annee").value(hasItem(DEFAULT_ANNEE)))
                .andExpect(jsonPath("$.[*].coefficient").value(hasItem(DEFAULT_COEFFICIENT)));
    }

    @Test
    @Transactional
    public void getEstDispense() throws Exception {
        // Initialize the database
        estDispenseRepository.saveAndFlush(estDispense);

        // Get the estDispense
        restEstDispenseMockMvc.perform(get("/api/est-dispenses/{id}", estDispense.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(estDispense.getId().intValue()))
            .andExpect(jsonPath("$.annee").value(DEFAULT_ANNEE))
            .andExpect(jsonPath("$.coefficient").value(DEFAULT_COEFFICIENT));
    }

    @Test
    @Transactional
    public void getNonExistingEstDispense() throws Exception {
        // Get the estDispense
        restEstDispenseMockMvc.perform(get("/api/est-dispenses/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEstDispense() throws Exception {
        // Initialize the database
        estDispenseRepository.saveAndFlush(estDispense);
        int databaseSizeBeforeUpdate = estDispenseRepository.findAll().size();

        // Update the estDispense
        EstDispense updatedEstDispense = estDispenseRepository.findOne(estDispense.getId());
        updatedEstDispense.setAnnee(UPDATED_ANNEE);
        updatedEstDispense.setCoefficient(UPDATED_COEFFICIENT);

        restEstDispenseMockMvc.perform(put("/api/est-dispenses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEstDispense)))
                .andExpect(status().isOk());

        // Validate the EstDispense in the database
        List<EstDispense> estDispenses = estDispenseRepository.findAll();
        assertThat(estDispenses).hasSize(databaseSizeBeforeUpdate);
        EstDispense testEstDispense = estDispenses.get(estDispenses.size() - 1);
        assertThat(testEstDispense.getAnnee()).isEqualTo(UPDATED_ANNEE);
        assertThat(testEstDispense.getCoefficient()).isEqualTo(UPDATED_COEFFICIENT);
    }

    @Test
    @Transactional
    public void deleteEstDispense() throws Exception {
        // Initialize the database
        estDispenseRepository.saveAndFlush(estDispense);
        int databaseSizeBeforeDelete = estDispenseRepository.findAll().size();

        // Get the estDispense
        restEstDispenseMockMvc.perform(delete("/api/est-dispenses/{id}", estDispense.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<EstDispense> estDispenses = estDispenseRepository.findAll();
        assertThat(estDispenses).hasSize(databaseSizeBeforeDelete - 1);
    }
}
