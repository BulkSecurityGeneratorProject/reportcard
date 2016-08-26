package cm.elsha.cardreport.web.rest;

import cm.elsha.cardreport.ReportcardApp;
import cm.elsha.cardreport.domain.Pays;
import cm.elsha.cardreport.repository.PaysRepository;

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
 * Test class for the PaysResource REST controller.
 *
 * @see PaysResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReportcardApp.class)
public class PaysResourceIntTest {
    private static final String DEFAULT_NOMPAYSFR = "AAAAA";
    private static final String UPDATED_NOMPAYSFR = "BBBBB";
    private static final String DEFAULT_NOMPAYSAN = "AAAAA";
    private static final String UPDATED_NOMPAYSAN = "BBBBB";
    private static final String DEFAULT_MINISTEREFR = "AAAAA";
    private static final String UPDATED_MINISTEREFR = "BBBBB";
    private static final String DEFAULT_MINISTEREAN = "AAAAA";
    private static final String UPDATED_MINISTEREAN = "BBBBB";
    private static final String DEFAULT_DEVISEFR = "AAAAA";
    private static final String UPDATED_DEVISEFR = "BBBBB";
    private static final String DEFAULT_DEVISEAN = "AAAAA";
    private static final String UPDATED_DEVISEAN = "BBBBB";

    @Inject
    private PaysRepository paysRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restPaysMockMvc;

    private Pays pays;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PaysResource paysResource = new PaysResource();
        ReflectionTestUtils.setField(paysResource, "paysRepository", paysRepository);
        this.restPaysMockMvc = MockMvcBuilders.standaloneSetup(paysResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pays createEntity(EntityManager em) {
        Pays pays = new Pays();
        pays = new Pays();
        pays.setNompaysfr(DEFAULT_NOMPAYSFR);
        pays.setNompaysan(DEFAULT_NOMPAYSAN);
        pays.setMinisterefr(DEFAULT_MINISTEREFR);
        pays.setMinisterean(DEFAULT_MINISTEREAN);
        pays.setDevisefr(DEFAULT_DEVISEFR);
        pays.setDevisean(DEFAULT_DEVISEAN);
        return pays;
    }

    @Before
    public void initTest() {
        pays = createEntity(em);
    }

    @Test
    @Transactional
    public void createPays() throws Exception {
        int databaseSizeBeforeCreate = paysRepository.findAll().size();

        // Create the Pays

        restPaysMockMvc.perform(post("/api/pays")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pays)))
                .andExpect(status().isCreated());

        // Validate the Pays in the database
        List<Pays> pays = paysRepository.findAll();
        assertThat(pays).hasSize(databaseSizeBeforeCreate + 1);
        Pays testPays = pays.get(pays.size() - 1);
        assertThat(testPays.getNompaysfr()).isEqualTo(DEFAULT_NOMPAYSFR);
        assertThat(testPays.getNompaysan()).isEqualTo(DEFAULT_NOMPAYSAN);
        assertThat(testPays.getMinisterefr()).isEqualTo(DEFAULT_MINISTEREFR);
        assertThat(testPays.getMinisterean()).isEqualTo(DEFAULT_MINISTEREAN);
        assertThat(testPays.getDevisefr()).isEqualTo(DEFAULT_DEVISEFR);
        assertThat(testPays.getDevisean()).isEqualTo(DEFAULT_DEVISEAN);
    }

    @Test
    @Transactional
    public void checkNompaysfrIsRequired() throws Exception {
        int databaseSizeBeforeTest = paysRepository.findAll().size();
        // set the field null
        pays.setNompaysfr(null);

        // Create the Pays, which fails.

        restPaysMockMvc.perform(post("/api/pays")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pays)))
                .andExpect(status().isBadRequest());

        List<Pays> pays = paysRepository.findAll();
        assertThat(pays).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNompaysanIsRequired() throws Exception {
        int databaseSizeBeforeTest = paysRepository.findAll().size();
        // set the field null
        pays.setNompaysan(null);

        // Create the Pays, which fails.

        restPaysMockMvc.perform(post("/api/pays")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pays)))
                .andExpect(status().isBadRequest());

        List<Pays> pays = paysRepository.findAll();
        assertThat(pays).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMinisterefrIsRequired() throws Exception {
        int databaseSizeBeforeTest = paysRepository.findAll().size();
        // set the field null
        pays.setMinisterefr(null);

        // Create the Pays, which fails.

        restPaysMockMvc.perform(post("/api/pays")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pays)))
                .andExpect(status().isBadRequest());

        List<Pays> pays = paysRepository.findAll();
        assertThat(pays).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMinistereanIsRequired() throws Exception {
        int databaseSizeBeforeTest = paysRepository.findAll().size();
        // set the field null
        pays.setMinisterean(null);

        // Create the Pays, which fails.

        restPaysMockMvc.perform(post("/api/pays")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pays)))
                .andExpect(status().isBadRequest());

        List<Pays> pays = paysRepository.findAll();
        assertThat(pays).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDevisefrIsRequired() throws Exception {
        int databaseSizeBeforeTest = paysRepository.findAll().size();
        // set the field null
        pays.setDevisefr(null);

        // Create the Pays, which fails.

        restPaysMockMvc.perform(post("/api/pays")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pays)))
                .andExpect(status().isBadRequest());

        List<Pays> pays = paysRepository.findAll();
        assertThat(pays).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDeviseanIsRequired() throws Exception {
        int databaseSizeBeforeTest = paysRepository.findAll().size();
        // set the field null
        pays.setDevisean(null);

        // Create the Pays, which fails.

        restPaysMockMvc.perform(post("/api/pays")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pays)))
                .andExpect(status().isBadRequest());

        List<Pays> pays = paysRepository.findAll();
        assertThat(pays).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPays() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        // Get all the pays
        restPaysMockMvc.perform(get("/api/pays?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(pays.getId().intValue())))
                .andExpect(jsonPath("$.[*].nompaysfr").value(hasItem(DEFAULT_NOMPAYSFR.toString())))
                .andExpect(jsonPath("$.[*].nompaysan").value(hasItem(DEFAULT_NOMPAYSAN.toString())))
                .andExpect(jsonPath("$.[*].ministerefr").value(hasItem(DEFAULT_MINISTEREFR.toString())))
                .andExpect(jsonPath("$.[*].ministerean").value(hasItem(DEFAULT_MINISTEREAN.toString())))
                .andExpect(jsonPath("$.[*].devisefr").value(hasItem(DEFAULT_DEVISEFR.toString())))
                .andExpect(jsonPath("$.[*].devisean").value(hasItem(DEFAULT_DEVISEAN.toString())));
    }

    @Test
    @Transactional
    public void getPays() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        // Get the pays
        restPaysMockMvc.perform(get("/api/pays/{id}", pays.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pays.getId().intValue()))
            .andExpect(jsonPath("$.nompaysfr").value(DEFAULT_NOMPAYSFR.toString()))
            .andExpect(jsonPath("$.nompaysan").value(DEFAULT_NOMPAYSAN.toString()))
            .andExpect(jsonPath("$.ministerefr").value(DEFAULT_MINISTEREFR.toString()))
            .andExpect(jsonPath("$.ministerean").value(DEFAULT_MINISTEREAN.toString()))
            .andExpect(jsonPath("$.devisefr").value(DEFAULT_DEVISEFR.toString()))
            .andExpect(jsonPath("$.devisean").value(DEFAULT_DEVISEAN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPays() throws Exception {
        // Get the pays
        restPaysMockMvc.perform(get("/api/pays/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePays() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);
        int databaseSizeBeforeUpdate = paysRepository.findAll().size();

        // Update the pays
        Pays updatedPays = paysRepository.findOne(pays.getId());
        updatedPays.setNompaysfr(UPDATED_NOMPAYSFR);
        updatedPays.setNompaysan(UPDATED_NOMPAYSAN);
        updatedPays.setMinisterefr(UPDATED_MINISTEREFR);
        updatedPays.setMinisterean(UPDATED_MINISTEREAN);
        updatedPays.setDevisefr(UPDATED_DEVISEFR);
        updatedPays.setDevisean(UPDATED_DEVISEAN);

        restPaysMockMvc.perform(put("/api/pays")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPays)))
                .andExpect(status().isOk());

        // Validate the Pays in the database
        List<Pays> pays = paysRepository.findAll();
        assertThat(pays).hasSize(databaseSizeBeforeUpdate);
        Pays testPays = pays.get(pays.size() - 1);
        assertThat(testPays.getNompaysfr()).isEqualTo(UPDATED_NOMPAYSFR);
        assertThat(testPays.getNompaysan()).isEqualTo(UPDATED_NOMPAYSAN);
        assertThat(testPays.getMinisterefr()).isEqualTo(UPDATED_MINISTEREFR);
        assertThat(testPays.getMinisterean()).isEqualTo(UPDATED_MINISTEREAN);
        assertThat(testPays.getDevisefr()).isEqualTo(UPDATED_DEVISEFR);
        assertThat(testPays.getDevisean()).isEqualTo(UPDATED_DEVISEAN);
    }

    @Test
    @Transactional
    public void deletePays() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);
        int databaseSizeBeforeDelete = paysRepository.findAll().size();

        // Get the pays
        restPaysMockMvc.perform(delete("/api/pays/{id}", pays.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Pays> pays = paysRepository.findAll();
        assertThat(pays).hasSize(databaseSizeBeforeDelete - 1);
    }
}
