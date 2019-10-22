package ai.dfx.labs.microlarp.web.rest;

import ai.dfx.labs.microlarp.MicrolarpingApp;
import ai.dfx.labs.microlarp.domain.Instance;
import ai.dfx.labs.microlarp.repository.InstanceRepository;
import ai.dfx.labs.microlarp.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static ai.dfx.labs.microlarp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link InstanceResource} REST controller.
 */
@SpringBootTest(classes = MicrolarpingApp.class)
public class InstanceResourceIT {

    @Autowired
    private InstanceRepository instanceRepository;

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

    private MockMvc restInstanceMockMvc;

    private Instance instance;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InstanceResource instanceResource = new InstanceResource(instanceRepository);
        this.restInstanceMockMvc = MockMvcBuilders.standaloneSetup(instanceResource)
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
    public static Instance createEntity(EntityManager em) {
        Instance instance = new Instance();
        return instance;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Instance createUpdatedEntity(EntityManager em) {
        Instance instance = new Instance();
        return instance;
    }

    @BeforeEach
    public void initTest() {
        instance = createEntity(em);
    }

    @Test
    @Transactional
    public void createInstance() throws Exception {
        int databaseSizeBeforeCreate = instanceRepository.findAll().size();

        // Create the Instance
        restInstanceMockMvc.perform(post("/api/instances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(instance)))
            .andExpect(status().isCreated());

        // Validate the Instance in the database
        List<Instance> instanceList = instanceRepository.findAll();
        assertThat(instanceList).hasSize(databaseSizeBeforeCreate + 1);
        Instance testInstance = instanceList.get(instanceList.size() - 1);
    }

    @Test
    @Transactional
    public void createInstanceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = instanceRepository.findAll().size();

        // Create the Instance with an existing ID
        instance.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInstanceMockMvc.perform(post("/api/instances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(instance)))
            .andExpect(status().isBadRequest());

        // Validate the Instance in the database
        List<Instance> instanceList = instanceRepository.findAll();
        assertThat(instanceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllInstances() throws Exception {
        // Initialize the database
        instanceRepository.saveAndFlush(instance);

        // Get all the instanceList
        restInstanceMockMvc.perform(get("/api/instances?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(instance.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getInstance() throws Exception {
        // Initialize the database
        instanceRepository.saveAndFlush(instance);

        // Get the instance
        restInstanceMockMvc.perform(get("/api/instances/{id}", instance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(instance.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingInstance() throws Exception {
        // Get the instance
        restInstanceMockMvc.perform(get("/api/instances/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInstance() throws Exception {
        // Initialize the database
        instanceRepository.saveAndFlush(instance);

        int databaseSizeBeforeUpdate = instanceRepository.findAll().size();

        // Update the instance
        Instance updatedInstance = instanceRepository.findById(instance.getId()).get();
        // Disconnect from session so that the updates on updatedInstance are not directly saved in db
        em.detach(updatedInstance);

        restInstanceMockMvc.perform(put("/api/instances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedInstance)))
            .andExpect(status().isOk());

        // Validate the Instance in the database
        List<Instance> instanceList = instanceRepository.findAll();
        assertThat(instanceList).hasSize(databaseSizeBeforeUpdate);
        Instance testInstance = instanceList.get(instanceList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingInstance() throws Exception {
        int databaseSizeBeforeUpdate = instanceRepository.findAll().size();

        // Create the Instance

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInstanceMockMvc.perform(put("/api/instances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(instance)))
            .andExpect(status().isBadRequest());

        // Validate the Instance in the database
        List<Instance> instanceList = instanceRepository.findAll();
        assertThat(instanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInstance() throws Exception {
        // Initialize the database
        instanceRepository.saveAndFlush(instance);

        int databaseSizeBeforeDelete = instanceRepository.findAll().size();

        // Delete the instance
        restInstanceMockMvc.perform(delete("/api/instances/{id}", instance.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Instance> instanceList = instanceRepository.findAll();
        assertThat(instanceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Instance.class);
        Instance instance1 = new Instance();
        instance1.setId(1L);
        Instance instance2 = new Instance();
        instance2.setId(instance1.getId());
        assertThat(instance1).isEqualTo(instance2);
        instance2.setId(2L);
        assertThat(instance1).isNotEqualTo(instance2);
        instance1.setId(null);
        assertThat(instance1).isNotEqualTo(instance2);
    }
}
