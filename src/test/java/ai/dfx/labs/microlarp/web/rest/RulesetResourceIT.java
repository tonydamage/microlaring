package ai.dfx.labs.microlarp.web.rest;

import ai.dfx.labs.microlarp.MicrolarpingApp;
import ai.dfx.labs.microlarp.domain.Ruleset;
import ai.dfx.labs.microlarp.repository.RulesetRepository;
import ai.dfx.labs.microlarp.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static ai.dfx.labs.microlarp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link RulesetResource} REST controller.
 */
@SpringBootTest(classes = MicrolarpingApp.class)
public class RulesetResourceIT {

    @Autowired
    private RulesetRepository rulesetRepository;

    @Mock
    private RulesetRepository rulesetRepositoryMock;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restRulesetMockMvc;

    private Ruleset ruleset;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RulesetResource rulesetResource = new RulesetResource(rulesetRepository);
        this.restRulesetMockMvc = MockMvcBuilders.standaloneSetup(rulesetResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ruleset createEntity(EntityManager em) {
        Ruleset ruleset = new Ruleset();
        return ruleset;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ruleset createUpdatedEntity(EntityManager em) {
        Ruleset ruleset = new Ruleset();
        return ruleset;
    }

    @BeforeEach
    public void initTest() {
        ruleset = createEntity(em);
    }

    @Test
    @Transactional
    public void createRuleset() throws Exception {
        int databaseSizeBeforeCreate = rulesetRepository.findAll().size();

        // Create the Ruleset
        restRulesetMockMvc.perform(post("/api/rulesets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ruleset)))
            .andExpect(status().isCreated());

        // Validate the Ruleset in the database
        List<Ruleset> rulesetList = rulesetRepository.findAll();
        assertThat(rulesetList).hasSize(databaseSizeBeforeCreate + 1);
        Ruleset testRuleset = rulesetList.get(rulesetList.size() - 1);
    }

    @Test
    @Transactional
    public void createRulesetWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rulesetRepository.findAll().size();

        // Create the Ruleset with an existing ID
        ruleset.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRulesetMockMvc.perform(post("/api/rulesets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ruleset)))
            .andExpect(status().isBadRequest());

        // Validate the Ruleset in the database
        List<Ruleset> rulesetList = rulesetRepository.findAll();
        assertThat(rulesetList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllRulesets() throws Exception {
        // Initialize the database
        rulesetRepository.saveAndFlush(ruleset);

        // Get all the rulesetList
        restRulesetMockMvc.perform(get("/api/rulesets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ruleset.getId().intValue())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllRulesetsWithEagerRelationshipsIsEnabled() throws Exception {
        RulesetResource rulesetResource = new RulesetResource(rulesetRepositoryMock);
        when(rulesetRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restRulesetMockMvc = MockMvcBuilders.standaloneSetup(rulesetResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restRulesetMockMvc.perform(get("/api/rulesets?eagerload=true"))
        .andExpect(status().isOk());

        verify(rulesetRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllRulesetsWithEagerRelationshipsIsNotEnabled() throws Exception {
        RulesetResource rulesetResource = new RulesetResource(rulesetRepositoryMock);
            when(rulesetRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restRulesetMockMvc = MockMvcBuilders.standaloneSetup(rulesetResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restRulesetMockMvc.perform(get("/api/rulesets?eagerload=true"))
        .andExpect(status().isOk());

            verify(rulesetRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getRuleset() throws Exception {
        // Initialize the database
        rulesetRepository.saveAndFlush(ruleset);

        // Get the ruleset
        restRulesetMockMvc.perform(get("/api/rulesets/{id}", ruleset.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ruleset.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingRuleset() throws Exception {
        // Get the ruleset
        restRulesetMockMvc.perform(get("/api/rulesets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRuleset() throws Exception {
        // Initialize the database
        rulesetRepository.saveAndFlush(ruleset);

        int databaseSizeBeforeUpdate = rulesetRepository.findAll().size();

        // Update the ruleset
        Ruleset updatedRuleset = rulesetRepository.findById(ruleset.getId()).get();
        // Disconnect from session so that the updates on updatedRuleset are not directly saved in db
        em.detach(updatedRuleset);

        restRulesetMockMvc.perform(put("/api/rulesets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRuleset)))
            .andExpect(status().isOk());

        // Validate the Ruleset in the database
        List<Ruleset> rulesetList = rulesetRepository.findAll();
        assertThat(rulesetList).hasSize(databaseSizeBeforeUpdate);
        Ruleset testRuleset = rulesetList.get(rulesetList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingRuleset() throws Exception {
        int databaseSizeBeforeUpdate = rulesetRepository.findAll().size();

        // Create the Ruleset

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRulesetMockMvc.perform(put("/api/rulesets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ruleset)))
            .andExpect(status().isBadRequest());

        // Validate the Ruleset in the database
        List<Ruleset> rulesetList = rulesetRepository.findAll();
        assertThat(rulesetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRuleset() throws Exception {
        // Initialize the database
        rulesetRepository.saveAndFlush(ruleset);

        int databaseSizeBeforeDelete = rulesetRepository.findAll().size();

        // Delete the ruleset
        restRulesetMockMvc.perform(delete("/api/rulesets/{id}", ruleset.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ruleset> rulesetList = rulesetRepository.findAll();
        assertThat(rulesetList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ruleset.class);
        Ruleset ruleset1 = new Ruleset();
        ruleset1.setId(1L);
        Ruleset ruleset2 = new Ruleset();
        ruleset2.setId(ruleset1.getId());
        assertThat(ruleset1).isEqualTo(ruleset2);
        ruleset2.setId(2L);
        assertThat(ruleset1).isNotEqualTo(ruleset2);
        ruleset1.setId(null);
        assertThat(ruleset1).isNotEqualTo(ruleset2);
    }
}
