package com.contacto.agenda.web.rest;

import com.contacto.agenda.TestApp;
import com.contacto.agenda.domain.Contacto;
import com.contacto.agenda.repository.ContactoRepository;
import com.contacto.agenda.web.rest.errors.ExceptionTranslator;

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

import static com.contacto.agenda.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ContactoResource} REST controller.
 */
@SpringBootTest(classes = TestApp.class)
public class ContactoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Integer DEFAULT_CELULAR = 1;
    private static final Integer UPDATED_CELULAR = 2;

    @Autowired
    private ContactoRepository contactoRepository;

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

    private MockMvc restContactoMockMvc;

    private Contacto contacto;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ContactoResource contactoResource = new ContactoResource(contactoRepository);
        this.restContactoMockMvc = MockMvcBuilders.standaloneSetup(contactoResource)
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
    public static Contacto createEntity(EntityManager em) {
        Contacto contacto = new Contacto()
            .nombre(DEFAULT_NOMBRE)
            .celular(DEFAULT_CELULAR);
        return contacto;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contacto createUpdatedEntity(EntityManager em) {
        Contacto contacto = new Contacto()
            .nombre(UPDATED_NOMBRE)
            .celular(UPDATED_CELULAR);
        return contacto;
    }

    @BeforeEach
    public void initTest() {
        contacto = createEntity(em);
    }

    @Test
    @Transactional
    public void createContacto() throws Exception {
        int databaseSizeBeforeCreate = contactoRepository.findAll().size();

        // Create the Contacto
        restContactoMockMvc.perform(post("/api/contactos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contacto)))
            .andExpect(status().isCreated());

        // Validate the Contacto in the database
        List<Contacto> contactoList = contactoRepository.findAll();
        assertThat(contactoList).hasSize(databaseSizeBeforeCreate + 1);
        Contacto testContacto = contactoList.get(contactoList.size() - 1);
        assertThat(testContacto.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testContacto.getCelular()).isEqualTo(DEFAULT_CELULAR);
    }

    @Test
    @Transactional
    public void createContactoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contactoRepository.findAll().size();

        // Create the Contacto with an existing ID
        contacto.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactoMockMvc.perform(post("/api/contactos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contacto)))
            .andExpect(status().isBadRequest());

        // Validate the Contacto in the database
        List<Contacto> contactoList = contactoRepository.findAll();
        assertThat(contactoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactoRepository.findAll().size();
        // set the field null
        contacto.setNombre(null);

        // Create the Contacto, which fails.

        restContactoMockMvc.perform(post("/api/contactos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contacto)))
            .andExpect(status().isBadRequest());

        List<Contacto> contactoList = contactoRepository.findAll();
        assertThat(contactoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllContactos() throws Exception {
        // Initialize the database
        contactoRepository.saveAndFlush(contacto);

        // Get all the contactoList
        restContactoMockMvc.perform(get("/api/contactos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contacto.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].celular").value(hasItem(DEFAULT_CELULAR)));
    }
    
    @Test
    @Transactional
    public void getContacto() throws Exception {
        // Initialize the database
        contactoRepository.saveAndFlush(contacto);

        // Get the contacto
        restContactoMockMvc.perform(get("/api/contactos/{id}", contacto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(contacto.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.celular").value(DEFAULT_CELULAR));
    }

    @Test
    @Transactional
    public void getNonExistingContacto() throws Exception {
        // Get the contacto
        restContactoMockMvc.perform(get("/api/contactos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContacto() throws Exception {
        // Initialize the database
        contactoRepository.saveAndFlush(contacto);

        int databaseSizeBeforeUpdate = contactoRepository.findAll().size();

        // Update the contacto
        Contacto updatedContacto = contactoRepository.findById(contacto.getId()).get();
        // Disconnect from session so that the updates on updatedContacto are not directly saved in db
        em.detach(updatedContacto);
        updatedContacto
            .nombre(UPDATED_NOMBRE)
            .celular(UPDATED_CELULAR);

        restContactoMockMvc.perform(put("/api/contactos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedContacto)))
            .andExpect(status().isOk());

        // Validate the Contacto in the database
        List<Contacto> contactoList = contactoRepository.findAll();
        assertThat(contactoList).hasSize(databaseSizeBeforeUpdate);
        Contacto testContacto = contactoList.get(contactoList.size() - 1);
        assertThat(testContacto.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testContacto.getCelular()).isEqualTo(UPDATED_CELULAR);
    }

    @Test
    @Transactional
    public void updateNonExistingContacto() throws Exception {
        int databaseSizeBeforeUpdate = contactoRepository.findAll().size();

        // Create the Contacto

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactoMockMvc.perform(put("/api/contactos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contacto)))
            .andExpect(status().isBadRequest());

        // Validate the Contacto in the database
        List<Contacto> contactoList = contactoRepository.findAll();
        assertThat(contactoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteContacto() throws Exception {
        // Initialize the database
        contactoRepository.saveAndFlush(contacto);

        int databaseSizeBeforeDelete = contactoRepository.findAll().size();

        // Delete the contacto
        restContactoMockMvc.perform(delete("/api/contactos/{id}", contacto.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Contacto> contactoList = contactoRepository.findAll();
        assertThat(contactoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
