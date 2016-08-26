package cm.elsha.cardreport.web.rest;

import cm.elsha.cardreport.ReportcardApp;
import cm.elsha.cardreport.domain.Categorie;
import cm.elsha.cardreport.repository.CategorieRepository;

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
 * Test class for the CategorieResource REST controller.
 *
 * @see CategorieResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReportcardApp.class)
public class CategorieResourceIntTest {
    private static final String DEFAULT_LIBELLE = "AAAAA";
    private static final String UPDATED_LIBELLE = "BBBBB";

    @Inject
    private CategorieRepository categorieRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCategorieMockMvc;

    private Categorie categorie;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CategorieResource categorieResource = new CategorieResource();
        ReflectionTestUtils.setField(categorieResource, "categorieRepository", categorieRepository);
        this.restCategorieMockMvc = MockMvcBuilders.standaloneSetup(categorieResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Categorie createEntity(EntityManager em) {
        Categorie categorie = new Categorie();
        categorie = new Categorie();
        categorie.setLibelle(DEFAULT_LIBELLE);
        return categorie;
    }

    @Before
    public void initTest() {
        categorie = createEntity(em);
    }

    @Test
    @Transactional
    public void createCategorie() throws Exception {
        int databaseSizeBeforeCreate = categorieRepository.findAll().size();

        // Create the Categorie

        restCategorieMockMvc.perform(post("/api/categories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(categorie)))
                .andExpect(status().isCreated());

        // Validate the Categorie in the database
        List<Categorie> categories = categorieRepository.findAll();
        assertThat(categories).hasSize(databaseSizeBeforeCreate + 1);
        Categorie testCategorie = categories.get(categories.size() - 1);
        assertThat(testCategorie.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = categorieRepository.findAll().size();
        // set the field null
        categorie.setLibelle(null);

        // Create the Categorie, which fails.

        restCategorieMockMvc.perform(post("/api/categories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(categorie)))
                .andExpect(status().isBadRequest());

        List<Categorie> categories = categorieRepository.findAll();
        assertThat(categories).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCategories() throws Exception {
        // Initialize the database
        categorieRepository.saveAndFlush(categorie);

        // Get all the categories
        restCategorieMockMvc.perform(get("/api/categories?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(categorie.getId().intValue())))
                .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())));
    }

    @Test
    @Transactional
    public void getCategorie() throws Exception {
        // Initialize the database
        categorieRepository.saveAndFlush(categorie);

        // Get the categorie
        restCategorieMockMvc.perform(get("/api/categories/{id}", categorie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(categorie.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCategorie() throws Exception {
        // Get the categorie
        restCategorieMockMvc.perform(get("/api/categories/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCategorie() throws Exception {
        // Initialize the database
        categorieRepository.saveAndFlush(categorie);
        int databaseSizeBeforeUpdate = categorieRepository.findAll().size();

        // Update the categorie
        Categorie updatedCategorie = categorieRepository.findOne(categorie.getId());
        updatedCategorie.setLibelle(UPDATED_LIBELLE);

        restCategorieMockMvc.perform(put("/api/categories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCategorie)))
                .andExpect(status().isOk());

        // Validate the Categorie in the database
        List<Categorie> categories = categorieRepository.findAll();
        assertThat(categories).hasSize(databaseSizeBeforeUpdate);
        Categorie testCategorie = categories.get(categories.size() - 1);
        assertThat(testCategorie.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void deleteCategorie() throws Exception {
        // Initialize the database
        categorieRepository.saveAndFlush(categorie);
        int databaseSizeBeforeDelete = categorieRepository.findAll().size();

        // Get the categorie
        restCategorieMockMvc.perform(delete("/api/categories/{id}", categorie.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Categorie> categories = categorieRepository.findAll();
        assertThat(categories).hasSize(databaseSizeBeforeDelete - 1);
    }
}
