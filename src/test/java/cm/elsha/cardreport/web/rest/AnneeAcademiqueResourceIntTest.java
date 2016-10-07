package cm.elsha.cardreport.web.rest;

import cm.elsha.cardreport.ReportcardApp;
import cm.elsha.cardreport.domain.AnneeAcademique;
import cm.elsha.cardreport.repository.AnneeAcademiqueRepository;

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
 * Test class for the AnneeAcademiqueResource REST controller.
 *
 * @see AnneeAcademiqueResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReportcardApp.class)
public class AnneeAcademiqueResourceIntTest {

    private static final Integer DEFAULT_ANNEEDEBUT = 2016;
    private static final Integer UPDATED_ANNEEDEBUT = 2017;

    private static final Integer DEFAULT_ANNEEFIN = 2017;
    private static final Integer UPDATED_ANNEEFIN = 2018;

    @Inject
    private AnneeAcademiqueRepository anneeAcademiqueRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restAnneeAcademiqueMockMvc;

    private AnneeAcademique anneeAcademique;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AnneeAcademiqueResource anneeAcademiqueResource = new AnneeAcademiqueResource();
        ReflectionTestUtils.setField(anneeAcademiqueResource, "anneeAcademiqueRepository", anneeAcademiqueRepository);
        this.restAnneeAcademiqueMockMvc = MockMvcBuilders.standaloneSetup(anneeAcademiqueResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnneeAcademique createEntity(EntityManager em) {
        AnneeAcademique anneeAcademique = new AnneeAcademique();
        anneeAcademique = new AnneeAcademique();
        anneeAcademique.setAnneedebut(DEFAULT_ANNEEDEBUT);
        anneeAcademique.setAnneefin(DEFAULT_ANNEEFIN);
        return anneeAcademique;
    }

    @Before
    public void initTest() {
        anneeAcademique = createEntity(em);
    }

    @Test
    @Transactional
    public void createAnneeAcademique() throws Exception {
        int databaseSizeBeforeCreate = anneeAcademiqueRepository.findAll().size();

        // Create the AnneeAcademique

        restAnneeAcademiqueMockMvc.perform(post("/api/annee-academiques")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(anneeAcademique)))
                .andExpect(status().isCreated());

        // Validate the AnneeAcademique in the database
        List<AnneeAcademique> anneeAcademiques = anneeAcademiqueRepository.findAll();
        assertThat(anneeAcademiques).hasSize(databaseSizeBeforeCreate + 1);
        AnneeAcademique testAnneeAcademique = anneeAcademiques.get(anneeAcademiques.size() - 1);
        assertThat(testAnneeAcademique.getAnneedebut()).isEqualTo(DEFAULT_ANNEEDEBUT);
        assertThat(testAnneeAcademique.getAnneefin()).isEqualTo(DEFAULT_ANNEEFIN);
    }

    @Test
    @Transactional
    public void checkAnneedebutIsRequired() throws Exception {
        int databaseSizeBeforeTest = anneeAcademiqueRepository.findAll().size();
        // set the field null
        anneeAcademique.setAnneedebut(null);

        // Create the AnneeAcademique, which fails.

        restAnneeAcademiqueMockMvc.perform(post("/api/annee-academiques")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(anneeAcademique)))
                .andExpect(status().isBadRequest());

        List<AnneeAcademique> anneeAcademiques = anneeAcademiqueRepository.findAll();
        assertThat(anneeAcademiques).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAnneefinIsRequired() throws Exception {
        int databaseSizeBeforeTest = anneeAcademiqueRepository.findAll().size();
        // set the field null
        anneeAcademique.setAnneefin(null);

        // Create the AnneeAcademique, which fails.

        restAnneeAcademiqueMockMvc.perform(post("/api/annee-academiques")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(anneeAcademique)))
                .andExpect(status().isBadRequest());

        List<AnneeAcademique> anneeAcademiques = anneeAcademiqueRepository.findAll();
        assertThat(anneeAcademiques).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAnneeAcademiques() throws Exception {
        // Initialize the database
        anneeAcademiqueRepository.saveAndFlush(anneeAcademique);

        // Get all the anneeAcademiques
        restAnneeAcademiqueMockMvc.perform(get("/api/annee-academiques?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(anneeAcademique.getId().intValue())))
                .andExpect(jsonPath("$.[*].anneedebut").value(hasItem(DEFAULT_ANNEEDEBUT)))
                .andExpect(jsonPath("$.[*].anneefin").value(hasItem(DEFAULT_ANNEEFIN)));
    }

    @Test
    @Transactional
    public void getAnneeAcademique() throws Exception {
        // Initialize the database
        anneeAcademiqueRepository.saveAndFlush(anneeAcademique);

        // Get the anneeAcademique
        restAnneeAcademiqueMockMvc.perform(get("/api/annee-academiques/{id}", anneeAcademique.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(anneeAcademique.getId().intValue()))
            .andExpect(jsonPath("$.anneedebut").value(DEFAULT_ANNEEDEBUT))
            .andExpect(jsonPath("$.anneefin").value(DEFAULT_ANNEEFIN));
    }

    @Test
    @Transactional
    public void getNonExistingAnneeAcademique() throws Exception {
        // Get the anneeAcademique
        restAnneeAcademiqueMockMvc.perform(get("/api/annee-academiques/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnneeAcademique() throws Exception {
        // Initialize the database
        anneeAcademiqueRepository.saveAndFlush(anneeAcademique);
        int databaseSizeBeforeUpdate = anneeAcademiqueRepository.findAll().size();

        // Update the anneeAcademique
        AnneeAcademique updatedAnneeAcademique = anneeAcademiqueRepository.findOne(anneeAcademique.getId());
        updatedAnneeAcademique.setAnneedebut(UPDATED_ANNEEDEBUT);
        updatedAnneeAcademique.setAnneefin(UPDATED_ANNEEFIN);

        restAnneeAcademiqueMockMvc.perform(put("/api/annee-academiques")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAnneeAcademique)))
                .andExpect(status().isOk());

        // Validate the AnneeAcademique in the database
        List<AnneeAcademique> anneeAcademiques = anneeAcademiqueRepository.findAll();
        assertThat(anneeAcademiques).hasSize(databaseSizeBeforeUpdate);
        AnneeAcademique testAnneeAcademique = anneeAcademiques.get(anneeAcademiques.size() - 1);
        assertThat(testAnneeAcademique.getAnneedebut()).isEqualTo(UPDATED_ANNEEDEBUT);
        assertThat(testAnneeAcademique.getAnneefin()).isEqualTo(UPDATED_ANNEEFIN);
    }

    @Test
    @Transactional
    public void deleteAnneeAcademique() throws Exception {
        // Initialize the database
        anneeAcademiqueRepository.saveAndFlush(anneeAcademique);
        int databaseSizeBeforeDelete = anneeAcademiqueRepository.findAll().size();

        // Get the anneeAcademique
        restAnneeAcademiqueMockMvc.perform(delete("/api/annee-academiques/{id}", anneeAcademique.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<AnneeAcademique> anneeAcademiques = anneeAcademiqueRepository.findAll();
        assertThat(anneeAcademiques).hasSize(databaseSizeBeforeDelete - 1);
    }
}
