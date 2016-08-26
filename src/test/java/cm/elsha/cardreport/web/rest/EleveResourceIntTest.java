package cm.elsha.cardreport.web.rest;

import cm.elsha.cardreport.ReportcardApp;
import cm.elsha.cardreport.domain.Eleve;
import cm.elsha.cardreport.repository.EleveRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cm.elsha.cardreport.domain.enumeration.Sexe;
/**
 * Test class for the EleveResource REST controller.
 *
 * @see EleveResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReportcardApp.class)
public class EleveResourceIntTest {
    private static final String DEFAULT_MATRICULE = "AAAAA";
    private static final String UPDATED_MATRICULE = "BBBBB";
    private static final String DEFAULT_NOM = "AAAAA";
    private static final String UPDATED_NOM = "BBBBB";
    private static final String DEFAULT_PRENOM = "AAAAA";
    private static final String UPDATED_PRENOM = "BBBBB";

    private static final LocalDate DEFAULT_DATENAISSANCE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATENAISSANCE = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_LIEUNAISSANCE = "AAAAA";
    private static final String UPDATED_LIEUNAISSANCE = "BBBBB";

    private static final Sexe DEFAULT_SEXE = Sexe.MASCULIN;
    private static final Sexe UPDATED_SEXE = Sexe.FEMININ;

    @Inject
    private EleveRepository eleveRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restEleveMockMvc;

    private Eleve eleve;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EleveResource eleveResource = new EleveResource();
        ReflectionTestUtils.setField(eleveResource, "eleveRepository", eleveRepository);
        this.restEleveMockMvc = MockMvcBuilders.standaloneSetup(eleveResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Eleve createEntity(EntityManager em) {
        Eleve eleve = new Eleve();
        eleve = new Eleve();
        eleve.setMatricule(DEFAULT_MATRICULE);
        eleve.setNom(DEFAULT_NOM);
        eleve.setPrenom(DEFAULT_PRENOM);
        eleve.setDatenaissance(DEFAULT_DATENAISSANCE);
        eleve.setLieunaissance(DEFAULT_LIEUNAISSANCE);
        eleve.setSexe(DEFAULT_SEXE);
        return eleve;
    }

    @Before
    public void initTest() {
        eleve = createEntity(em);
    }

    @Test
    @Transactional
    public void createEleve() throws Exception {
        int databaseSizeBeforeCreate = eleveRepository.findAll().size();

        // Create the Eleve

        restEleveMockMvc.perform(post("/api/eleves")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(eleve)))
                .andExpect(status().isCreated());

        // Validate the Eleve in the database
        List<Eleve> eleves = eleveRepository.findAll();
        assertThat(eleves).hasSize(databaseSizeBeforeCreate + 1);
        Eleve testEleve = eleves.get(eleves.size() - 1);
        assertThat(testEleve.getMatricule()).isEqualTo(DEFAULT_MATRICULE);
        assertThat(testEleve.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testEleve.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testEleve.getDatenaissance()).isEqualTo(DEFAULT_DATENAISSANCE);
        assertThat(testEleve.getLieunaissance()).isEqualTo(DEFAULT_LIEUNAISSANCE);
        assertThat(testEleve.getSexe()).isEqualTo(DEFAULT_SEXE);
    }

    @Test
    @Transactional
    public void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = eleveRepository.findAll().size();
        // set the field null
        eleve.setNom(null);

        // Create the Eleve, which fails.

        restEleveMockMvc.perform(post("/api/eleves")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(eleve)))
                .andExpect(status().isBadRequest());

        List<Eleve> eleves = eleveRepository.findAll();
        assertThat(eleves).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDatenaissanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = eleveRepository.findAll().size();
        // set the field null
        eleve.setDatenaissance(null);

        // Create the Eleve, which fails.

        restEleveMockMvc.perform(post("/api/eleves")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(eleve)))
                .andExpect(status().isBadRequest());

        List<Eleve> eleves = eleveRepository.findAll();
        assertThat(eleves).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEleves() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleves
        restEleveMockMvc.perform(get("/api/eleves?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(eleve.getId().intValue())))
                .andExpect(jsonPath("$.[*].matricule").value(hasItem(DEFAULT_MATRICULE.toString())))
                .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
                .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM.toString())))
                .andExpect(jsonPath("$.[*].datenaissance").value(hasItem(DEFAULT_DATENAISSANCE.toString())))
                .andExpect(jsonPath("$.[*].lieunaissance").value(hasItem(DEFAULT_LIEUNAISSANCE.toString())))
                .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())));
    }

    @Test
    @Transactional
    public void getEleve() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get the eleve
        restEleveMockMvc.perform(get("/api/eleves/{id}", eleve.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(eleve.getId().intValue()))
            .andExpect(jsonPath("$.matricule").value(DEFAULT_MATRICULE.toString()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM.toString()))
            .andExpect(jsonPath("$.datenaissance").value(DEFAULT_DATENAISSANCE.toString()))
            .andExpect(jsonPath("$.lieunaissance").value(DEFAULT_LIEUNAISSANCE.toString()))
            .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEleve() throws Exception {
        // Get the eleve
        restEleveMockMvc.perform(get("/api/eleves/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEleve() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);
        int databaseSizeBeforeUpdate = eleveRepository.findAll().size();

        // Update the eleve
        Eleve updatedEleve = eleveRepository.findOne(eleve.getId());
        updatedEleve.setMatricule(UPDATED_MATRICULE);
        updatedEleve.setNom(UPDATED_NOM);
        updatedEleve.setPrenom(UPDATED_PRENOM);
        updatedEleve.setDatenaissance(UPDATED_DATENAISSANCE);
        updatedEleve.setLieunaissance(UPDATED_LIEUNAISSANCE);
        updatedEleve.setSexe(UPDATED_SEXE);

        restEleveMockMvc.perform(put("/api/eleves")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEleve)))
                .andExpect(status().isOk());

        // Validate the Eleve in the database
        List<Eleve> eleves = eleveRepository.findAll();
        assertThat(eleves).hasSize(databaseSizeBeforeUpdate);
        Eleve testEleve = eleves.get(eleves.size() - 1);
        assertThat(testEleve.getMatricule()).isEqualTo(UPDATED_MATRICULE);
        assertThat(testEleve.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testEleve.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testEleve.getDatenaissance()).isEqualTo(UPDATED_DATENAISSANCE);
        assertThat(testEleve.getLieunaissance()).isEqualTo(UPDATED_LIEUNAISSANCE);
        assertThat(testEleve.getSexe()).isEqualTo(UPDATED_SEXE);
    }

    @Test
    @Transactional
    public void deleteEleve() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);
        int databaseSizeBeforeDelete = eleveRepository.findAll().size();

        // Get the eleve
        restEleveMockMvc.perform(delete("/api/eleves/{id}", eleve.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Eleve> eleves = eleveRepository.findAll();
        assertThat(eleves).hasSize(databaseSizeBeforeDelete - 1);
    }
}
