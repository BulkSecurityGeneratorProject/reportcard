package cm.elsha.cardreport.web.rest;

import cm.elsha.cardreport.ReportcardApp;
import cm.elsha.cardreport.domain.Matiere;
import cm.elsha.cardreport.repository.MatiereRepository;

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
 * Test class for the MatiereResource REST controller.
 *
 * @see MatiereResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReportcardApp.class)
public class MatiereResourceIntTest {
    private static final String DEFAULT_LIBELLE = "AAAAA";
    private static final String UPDATED_LIBELLE = "BBBBB";

    @Inject
    private MatiereRepository matiereRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restMatiereMockMvc;

    private Matiere matiere;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MatiereResource matiereResource = new MatiereResource();
        ReflectionTestUtils.setField(matiereResource, "matiereRepository", matiereRepository);
        this.restMatiereMockMvc = MockMvcBuilders.standaloneSetup(matiereResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Matiere createEntity(EntityManager em) {
        Matiere matiere = new Matiere();
        matiere = new Matiere();
        matiere.setLibelle(DEFAULT_LIBELLE);
        return matiere;
    }

    @Before
    public void initTest() {
        matiere = createEntity(em);
    }

    @Test
    @Transactional
    public void createMatiere() throws Exception {
        int databaseSizeBeforeCreate = matiereRepository.findAll().size();

        // Create the Matiere

        restMatiereMockMvc.perform(post("/api/matieres")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(matiere)))
                .andExpect(status().isCreated());

        // Validate the Matiere in the database
        List<Matiere> matieres = matiereRepository.findAll();
        assertThat(matieres).hasSize(databaseSizeBeforeCreate + 1);
        Matiere testMatiere = matieres.get(matieres.size() - 1);
        assertThat(testMatiere.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = matiereRepository.findAll().size();
        // set the field null
        matiere.setLibelle(null);

        // Create the Matiere, which fails.

        restMatiereMockMvc.perform(post("/api/matieres")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(matiere)))
                .andExpect(status().isBadRequest());

        List<Matiere> matieres = matiereRepository.findAll();
        assertThat(matieres).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMatieres() throws Exception {
        // Initialize the database
        matiereRepository.saveAndFlush(matiere);

        // Get all the matieres
        restMatiereMockMvc.perform(get("/api/matieres?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(matiere.getId().intValue())))
                .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())));
    }

    @Test
    @Transactional
    public void getMatiere() throws Exception {
        // Initialize the database
        matiereRepository.saveAndFlush(matiere);

        // Get the matiere
        restMatiereMockMvc.perform(get("/api/matieres/{id}", matiere.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(matiere.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMatiere() throws Exception {
        // Get the matiere
        restMatiereMockMvc.perform(get("/api/matieres/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMatiere() throws Exception {
        // Initialize the database
        matiereRepository.saveAndFlush(matiere);
        int databaseSizeBeforeUpdate = matiereRepository.findAll().size();

        // Update the matiere
        Matiere updatedMatiere = matiereRepository.findOne(matiere.getId());
        updatedMatiere.setLibelle(UPDATED_LIBELLE);

        restMatiereMockMvc.perform(put("/api/matieres")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMatiere)))
                .andExpect(status().isOk());

        // Validate the Matiere in the database
        List<Matiere> matieres = matiereRepository.findAll();
        assertThat(matieres).hasSize(databaseSizeBeforeUpdate);
        Matiere testMatiere = matieres.get(matieres.size() - 1);
        assertThat(testMatiere.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void deleteMatiere() throws Exception {
        // Initialize the database
        matiereRepository.saveAndFlush(matiere);
        int databaseSizeBeforeDelete = matiereRepository.findAll().size();

        // Get the matiere
        restMatiereMockMvc.perform(delete("/api/matieres/{id}", matiere.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Matiere> matieres = matiereRepository.findAll();
        assertThat(matieres).hasSize(databaseSizeBeforeDelete - 1);
    }
}
