package cm.elsha.cardreport.web.rest;

import cm.elsha.cardreport.ReportcardApp;
import cm.elsha.cardreport.domain.Inscrire;
import cm.elsha.cardreport.repository.InscrireRepository;

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
 * Test class for the InscrireResource REST controller.
 *
 * @see InscrireResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReportcardApp.class)
public class InscrireResourceIntTest {

    @Inject
    private InscrireRepository inscrireRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restInscrireMockMvc;

    private Inscrire inscrire;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InscrireResource inscrireResource = new InscrireResource();
        ReflectionTestUtils.setField(inscrireResource, "inscrireRepository", inscrireRepository);
        this.restInscrireMockMvc = MockMvcBuilders.standaloneSetup(inscrireResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inscrire createEntity(EntityManager em) {
        Inscrire inscrire = new Inscrire();
        inscrire = new Inscrire();
        return inscrire;
    }

    @Before
    public void initTest() {
        inscrire = createEntity(em);
    }

    @Test
    @Transactional
    public void createInscrire() throws Exception {
        int databaseSizeBeforeCreate = inscrireRepository.findAll().size();

        // Create the Inscrire

        restInscrireMockMvc.perform(post("/api/inscrires")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(inscrire)))
                .andExpect(status().isCreated());

        // Validate the Inscrire in the database
        List<Inscrire> inscrires = inscrireRepository.findAll();
        assertThat(inscrires).hasSize(databaseSizeBeforeCreate + 1);
        Inscrire testInscrire = inscrires.get(inscrires.size() - 1);
    }

    @Test
    @Transactional
    public void getAllInscrires() throws Exception {
        // Initialize the database
        inscrireRepository.saveAndFlush(inscrire);

        // Get all the inscrires
        restInscrireMockMvc.perform(get("/api/inscrires?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(inscrire.getId().intValue())));
    }

    @Test
    @Transactional
    public void getInscrire() throws Exception {
        // Initialize the database
        inscrireRepository.saveAndFlush(inscrire);

        // Get the inscrire
        restInscrireMockMvc.perform(get("/api/inscrires/{id}", inscrire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(inscrire.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingInscrire() throws Exception {
        // Get the inscrire
        restInscrireMockMvc.perform(get("/api/inscrires/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInscrire() throws Exception {
        // Initialize the database
        inscrireRepository.saveAndFlush(inscrire);
        int databaseSizeBeforeUpdate = inscrireRepository.findAll().size();

        // Update the inscrire
        Inscrire updatedInscrire = inscrireRepository.findOne(inscrire.getId());

        restInscrireMockMvc.perform(put("/api/inscrires")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedInscrire)))
                .andExpect(status().isOk());

        // Validate the Inscrire in the database
        List<Inscrire> inscrires = inscrireRepository.findAll();
        assertThat(inscrires).hasSize(databaseSizeBeforeUpdate);
        Inscrire testInscrire = inscrires.get(inscrires.size() - 1);
    }

    @Test
    @Transactional
    public void deleteInscrire() throws Exception {
        // Initialize the database
        inscrireRepository.saveAndFlush(inscrire);
        int databaseSizeBeforeDelete = inscrireRepository.findAll().size();

        // Get the inscrire
        restInscrireMockMvc.perform(delete("/api/inscrires/{id}", inscrire.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Inscrire> inscrires = inscrireRepository.findAll();
        assertThat(inscrires).hasSize(databaseSizeBeforeDelete - 1);
    }
}
