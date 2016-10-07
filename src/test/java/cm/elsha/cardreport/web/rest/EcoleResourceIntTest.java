package cm.elsha.cardreport.web.rest;

import cm.elsha.cardreport.ReportcardApp;
import cm.elsha.cardreport.domain.Ecole;
import cm.elsha.cardreport.repository.EcoleRepository;

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
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the EcoleResource REST controller.
 *
 * @see EcoleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReportcardApp.class)
public class EcoleResourceIntTest {
    private static final String DEFAULT_NOMFR = "AAAAA";
    private static final String UPDATED_NOMFR = "BBBBB";
    private static final String DEFAULT_NOMAN = "AAAAA";
    private static final String UPDATED_NOMAN = "BBBBB";
    private static final String DEFAULT_DEVISEFR = "AAAAA";
    private static final String UPDATED_DEVISEFR = "BBBBB";
    private static final String DEFAULT_DEVISEAN = "AAAAA";
    private static final String UPDATED_DEVISEAN = "BBBBB";
    private static final String DEFAULT_BOITEPOSTAL = "AAAAA";
    private static final String UPDATED_BOITEPOSTAL = "BBBBB";

    private static final byte[] DEFAULT_LOGO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_LOGO = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_LOGO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_LOGO_CONTENT_TYPE = "image/png";

    @Inject
    private EcoleRepository ecoleRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restEcoleMockMvc;

    private Ecole ecole;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EcoleResource ecoleResource = new EcoleResource();
        ReflectionTestUtils.setField(ecoleResource, "ecoleRepository", ecoleRepository);
        this.restEcoleMockMvc = MockMvcBuilders.standaloneSetup(ecoleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ecole createEntity(EntityManager em) {
        Ecole ecole = new Ecole();
        ecole = new Ecole();
        ecole.setNomfr(DEFAULT_NOMFR);
        ecole.setNoman(DEFAULT_NOMAN);
        ecole.setDevisefr(DEFAULT_DEVISEFR);
        ecole.setDevisean(DEFAULT_DEVISEAN);
        ecole.setBoitepostal(DEFAULT_BOITEPOSTAL);
        ecole.setLogo(DEFAULT_LOGO);
        ecole.setLogoContentType(DEFAULT_LOGO_CONTENT_TYPE);
        return ecole;
    }

    @Before
    public void initTest() {
        ecole = createEntity(em);
    }

    @Test
    @Transactional
    public void createEcole() throws Exception {
        int databaseSizeBeforeCreate = ecoleRepository.findAll().size();

        // Create the Ecole

        restEcoleMockMvc.perform(post("/api/ecoles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ecole)))
                .andExpect(status().isCreated());

        // Validate the Ecole in the database
        List<Ecole> ecoles = ecoleRepository.findAll();
        assertThat(ecoles).hasSize(databaseSizeBeforeCreate + 1);
        Ecole testEcole = ecoles.get(ecoles.size() - 1);
        assertThat(testEcole.getNomfr()).isEqualTo(DEFAULT_NOMFR);
        assertThat(testEcole.getNoman()).isEqualTo(DEFAULT_NOMAN);
        assertThat(testEcole.getDevisefr()).isEqualTo(DEFAULT_DEVISEFR);
        assertThat(testEcole.getDevisean()).isEqualTo(DEFAULT_DEVISEAN);
        assertThat(testEcole.getBoitepostal()).isEqualTo(DEFAULT_BOITEPOSTAL);
        assertThat(testEcole.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testEcole.getLogoContentType()).isEqualTo(DEFAULT_LOGO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void checkNomfrIsRequired() throws Exception {
        int databaseSizeBeforeTest = ecoleRepository.findAll().size();
        // set the field null
        ecole.setNomfr(null);

        // Create the Ecole, which fails.

        restEcoleMockMvc.perform(post("/api/ecoles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ecole)))
                .andExpect(status().isBadRequest());

        List<Ecole> ecoles = ecoleRepository.findAll();
        assertThat(ecoles).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNomanIsRequired() throws Exception {
        int databaseSizeBeforeTest = ecoleRepository.findAll().size();
        // set the field null
        ecole.setNoman(null);

        // Create the Ecole, which fails.

        restEcoleMockMvc.perform(post("/api/ecoles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ecole)))
                .andExpect(status().isBadRequest());

        List<Ecole> ecoles = ecoleRepository.findAll();
        assertThat(ecoles).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDevisefrIsRequired() throws Exception {
        int databaseSizeBeforeTest = ecoleRepository.findAll().size();
        // set the field null
        ecole.setDevisefr(null);

        // Create the Ecole, which fails.

        restEcoleMockMvc.perform(post("/api/ecoles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ecole)))
                .andExpect(status().isBadRequest());

        List<Ecole> ecoles = ecoleRepository.findAll();
        assertThat(ecoles).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDeviseanIsRequired() throws Exception {
        int databaseSizeBeforeTest = ecoleRepository.findAll().size();
        // set the field null
        ecole.setDevisean(null);

        // Create the Ecole, which fails.

        restEcoleMockMvc.perform(post("/api/ecoles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ecole)))
                .andExpect(status().isBadRequest());

        List<Ecole> ecoles = ecoleRepository.findAll();
        assertThat(ecoles).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEcoles() throws Exception {
        // Initialize the database
        ecoleRepository.saveAndFlush(ecole);

        // Get all the ecoles
        restEcoleMockMvc.perform(get("/api/ecoles?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(ecole.getId().intValue())))
                .andExpect(jsonPath("$.[*].nomfr").value(hasItem(DEFAULT_NOMFR.toString())))
                .andExpect(jsonPath("$.[*].noman").value(hasItem(DEFAULT_NOMAN.toString())))
                .andExpect(jsonPath("$.[*].devisefr").value(hasItem(DEFAULT_DEVISEFR.toString())))
                .andExpect(jsonPath("$.[*].devisean").value(hasItem(DEFAULT_DEVISEAN.toString())))
                .andExpect(jsonPath("$.[*].boitepostal").value(hasItem(DEFAULT_BOITEPOSTAL.toString())))
                .andExpect(jsonPath("$.[*].logoContentType").value(hasItem(DEFAULT_LOGO_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].logo").value(hasItem(Base64Utils.encodeToString(DEFAULT_LOGO))));
    }

    @Test
    @Transactional
    public void getEcole() throws Exception {
        // Initialize the database
        ecoleRepository.saveAndFlush(ecole);

        // Get the ecole
        restEcoleMockMvc.perform(get("/api/ecoles/{id}", ecole.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ecole.getId().intValue()))
            .andExpect(jsonPath("$.nomfr").value(DEFAULT_NOMFR.toString()))
            .andExpect(jsonPath("$.noman").value(DEFAULT_NOMAN.toString()))
            .andExpect(jsonPath("$.devisefr").value(DEFAULT_DEVISEFR.toString()))
            .andExpect(jsonPath("$.devisean").value(DEFAULT_DEVISEAN.toString()))
            .andExpect(jsonPath("$.boitepostal").value(DEFAULT_BOITEPOSTAL.toString()))
            .andExpect(jsonPath("$.logoContentType").value(DEFAULT_LOGO_CONTENT_TYPE))
            .andExpect(jsonPath("$.logo").value(Base64Utils.encodeToString(DEFAULT_LOGO)));
    }

    @Test
    @Transactional
    public void getNonExistingEcole() throws Exception {
        // Get the ecole
        restEcoleMockMvc.perform(get("/api/ecoles/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEcole() throws Exception {
        // Initialize the database
        ecoleRepository.saveAndFlush(ecole);
        int databaseSizeBeforeUpdate = ecoleRepository.findAll().size();

        // Update the ecole
        Ecole updatedEcole = ecoleRepository.findOne(ecole.getId());
        updatedEcole.setNomfr(UPDATED_NOMFR);
        updatedEcole.setNoman(UPDATED_NOMAN);
        updatedEcole.setDevisefr(UPDATED_DEVISEFR);
        updatedEcole.setDevisean(UPDATED_DEVISEAN);
        updatedEcole.setBoitepostal(UPDATED_BOITEPOSTAL);
        updatedEcole.setLogo(UPDATED_LOGO);
        updatedEcole.setLogoContentType(UPDATED_LOGO_CONTENT_TYPE);

        restEcoleMockMvc.perform(put("/api/ecoles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEcole)))
                .andExpect(status().isOk());

        // Validate the Ecole in the database
        List<Ecole> ecoles = ecoleRepository.findAll();
        assertThat(ecoles).hasSize(databaseSizeBeforeUpdate);
        Ecole testEcole = ecoles.get(ecoles.size() - 1);
        assertThat(testEcole.getNomfr()).isEqualTo(UPDATED_NOMFR);
        assertThat(testEcole.getNoman()).isEqualTo(UPDATED_NOMAN);
        assertThat(testEcole.getDevisefr()).isEqualTo(UPDATED_DEVISEFR);
        assertThat(testEcole.getDevisean()).isEqualTo(UPDATED_DEVISEAN);
        assertThat(testEcole.getBoitepostal()).isEqualTo(UPDATED_BOITEPOSTAL);
        assertThat(testEcole.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testEcole.getLogoContentType()).isEqualTo(UPDATED_LOGO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void deleteEcole() throws Exception {
        // Initialize the database
        ecoleRepository.saveAndFlush(ecole);
        int databaseSizeBeforeDelete = ecoleRepository.findAll().size();

        // Get the ecole
        restEcoleMockMvc.perform(delete("/api/ecoles/{id}", ecole.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Ecole> ecoles = ecoleRepository.findAll();
        assertThat(ecoles).hasSize(databaseSizeBeforeDelete - 1);
    }
}
