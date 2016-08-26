package cm.elsha.cardreport.web.rest;

import cm.elsha.cardreport.ReportcardApp;
import cm.elsha.cardreport.domain.Classe;
import cm.elsha.cardreport.repository.ClasseRepository;

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
 * Test class for the ClasseResource REST controller.
 *
 * @see ClasseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReportcardApp.class)
public class ClasseResourceIntTest {
    private static final String DEFAULT_NOM = "AAAAA";
    private static final String UPDATED_NOM = "BBBBB";

    private static final Integer DEFAULT_CYCLE = 1;
    private static final Integer UPDATED_CYCLE = 2;

    @Inject
    private ClasseRepository classeRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restClasseMockMvc;

    private Classe classe;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ClasseResource classeResource = new ClasseResource();
        ReflectionTestUtils.setField(classeResource, "classeRepository", classeRepository);
        this.restClasseMockMvc = MockMvcBuilders.standaloneSetup(classeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Classe createEntity(EntityManager em) {
        Classe classe = new Classe();
        classe = new Classe();
        classe.setNom(DEFAULT_NOM);
        classe.setCycle(DEFAULT_CYCLE);
        return classe;
    }

    @Before
    public void initTest() {
        classe = createEntity(em);
    }

    @Test
    @Transactional
    public void createClasse() throws Exception {
        int databaseSizeBeforeCreate = classeRepository.findAll().size();

        // Create the Classe

        restClasseMockMvc.perform(post("/api/classes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(classe)))
                .andExpect(status().isCreated());

        // Validate the Classe in the database
        List<Classe> classes = classeRepository.findAll();
        assertThat(classes).hasSize(databaseSizeBeforeCreate + 1);
        Classe testClasse = classes.get(classes.size() - 1);
        assertThat(testClasse.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testClasse.getCycle()).isEqualTo(DEFAULT_CYCLE);
    }

    @Test
    @Transactional
    public void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = classeRepository.findAll().size();
        // set the field null
        classe.setNom(null);

        // Create the Classe, which fails.

        restClasseMockMvc.perform(post("/api/classes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(classe)))
                .andExpect(status().isBadRequest());

        List<Classe> classes = classeRepository.findAll();
        assertThat(classes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCycleIsRequired() throws Exception {
        int databaseSizeBeforeTest = classeRepository.findAll().size();
        // set the field null
        classe.setCycle(null);

        // Create the Classe, which fails.

        restClasseMockMvc.perform(post("/api/classes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(classe)))
                .andExpect(status().isBadRequest());

        List<Classe> classes = classeRepository.findAll();
        assertThat(classes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllClasses() throws Exception {
        // Initialize the database
        classeRepository.saveAndFlush(classe);

        // Get all the classes
        restClasseMockMvc.perform(get("/api/classes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(classe.getId().intValue())))
                .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
                .andExpect(jsonPath("$.[*].cycle").value(hasItem(DEFAULT_CYCLE)));
    }

    @Test
    @Transactional
    public void getClasse() throws Exception {
        // Initialize the database
        classeRepository.saveAndFlush(classe);

        // Get the classe
        restClasseMockMvc.perform(get("/api/classes/{id}", classe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(classe.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.cycle").value(DEFAULT_CYCLE));
    }

    @Test
    @Transactional
    public void getNonExistingClasse() throws Exception {
        // Get the classe
        restClasseMockMvc.perform(get("/api/classes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClasse() throws Exception {
        // Initialize the database
        classeRepository.saveAndFlush(classe);
        int databaseSizeBeforeUpdate = classeRepository.findAll().size();

        // Update the classe
        Classe updatedClasse = classeRepository.findOne(classe.getId());
        updatedClasse.setNom(UPDATED_NOM);
        updatedClasse.setCycle(UPDATED_CYCLE);

        restClasseMockMvc.perform(put("/api/classes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedClasse)))
                .andExpect(status().isOk());

        // Validate the Classe in the database
        List<Classe> classes = classeRepository.findAll();
        assertThat(classes).hasSize(databaseSizeBeforeUpdate);
        Classe testClasse = classes.get(classes.size() - 1);
        assertThat(testClasse.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testClasse.getCycle()).isEqualTo(UPDATED_CYCLE);
    }

    @Test
    @Transactional
    public void deleteClasse() throws Exception {
        // Initialize the database
        classeRepository.saveAndFlush(classe);
        int databaseSizeBeforeDelete = classeRepository.findAll().size();

        // Get the classe
        restClasseMockMvc.perform(delete("/api/classes/{id}", classe.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Classe> classes = classeRepository.findAll();
        assertThat(classes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
