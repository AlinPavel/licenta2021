package com.licenta.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.licenta.IntegrationTest;
import com.licenta.domain.Catalog;
import com.licenta.repository.CatalogRepository;
import com.licenta.service.dto.CatalogDTO;
import com.licenta.service.mapper.CatalogMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CatalogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CatalogResourceIT {

    private static final Double DEFAULT_NOTA = 1D;
    private static final Double UPDATED_NOTA = 2D;

    private static final String ENTITY_API_URL = "/api/catalogs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{idCatalog}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CatalogRepository catalogRepository;

    @Autowired
    private CatalogMapper catalogMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCatalogMockMvc;

    private Catalog catalog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Catalog createEntity(EntityManager em) {
        Catalog catalog = new Catalog().nota(DEFAULT_NOTA);
        return catalog;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Catalog createUpdatedEntity(EntityManager em) {
        Catalog catalog = new Catalog().nota(UPDATED_NOTA);
        return catalog;
    }

    @BeforeEach
    public void initTest() {
        catalog = createEntity(em);
    }

    @Test
    @Transactional
    void createCatalog() throws Exception {
        int databaseSizeBeforeCreate = catalogRepository.findAll().size();
        // Create the Catalog
        CatalogDTO catalogDTO = catalogMapper.toDto(catalog);
        restCatalogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(catalogDTO)))
            .andExpect(status().isCreated());

        // Validate the Catalog in the database
        List<Catalog> catalogList = catalogRepository.findAll();
        assertThat(catalogList).hasSize(databaseSizeBeforeCreate + 1);
        Catalog testCatalog = catalogList.get(catalogList.size() - 1);
        assertThat(testCatalog.getNota()).isEqualTo(DEFAULT_NOTA);
    }

    @Test
    @Transactional
    void createCatalogWithExistingId() throws Exception {
        // Create the Catalog with an existing ID
        catalog.setIdCatalog(1L);
        CatalogDTO catalogDTO = catalogMapper.toDto(catalog);

        int databaseSizeBeforeCreate = catalogRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCatalogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(catalogDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Catalog in the database
        List<Catalog> catalogList = catalogRepository.findAll();
        assertThat(catalogList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCatalogs() throws Exception {
        // Initialize the database
        catalogRepository.saveAndFlush(catalog);

        // Get all the catalogList
        restCatalogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=idCatalog,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].idCatalog").value(hasItem(catalog.getIdCatalog().intValue())))
            .andExpect(jsonPath("$.[*].nota").value(hasItem(DEFAULT_NOTA.doubleValue())));
    }

    @Test
    @Transactional
    void getCatalog() throws Exception {
        // Initialize the database
        catalogRepository.saveAndFlush(catalog);

        // Get the catalog
        restCatalogMockMvc
            .perform(get(ENTITY_API_URL_ID, catalog.getIdCatalog()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.idCatalog").value(catalog.getIdCatalog().intValue()))
            .andExpect(jsonPath("$.nota").value(DEFAULT_NOTA.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingCatalog() throws Exception {
        // Get the catalog
        restCatalogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCatalog() throws Exception {
        // Initialize the database
        catalogRepository.saveAndFlush(catalog);

        int databaseSizeBeforeUpdate = catalogRepository.findAll().size();

        // Update the catalog
        Catalog updatedCatalog = catalogRepository.findById(catalog.getIdCatalog()).get();
        // Disconnect from session so that the updates on updatedCatalog are not directly saved in db
        em.detach(updatedCatalog);
        updatedCatalog.nota(UPDATED_NOTA);
        CatalogDTO catalogDTO = catalogMapper.toDto(updatedCatalog);

        restCatalogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, catalogDTO.getIdCatalog())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogDTO))
            )
            .andExpect(status().isOk());

        // Validate the Catalog in the database
        List<Catalog> catalogList = catalogRepository.findAll();
        assertThat(catalogList).hasSize(databaseSizeBeforeUpdate);
        Catalog testCatalog = catalogList.get(catalogList.size() - 1);
        assertThat(testCatalog.getNota()).isEqualTo(UPDATED_NOTA);
    }

    @Test
    @Transactional
    void putNonExistingCatalog() throws Exception {
        int databaseSizeBeforeUpdate = catalogRepository.findAll().size();
        catalog.setIdCatalog(count.incrementAndGet());

        // Create the Catalog
        CatalogDTO catalogDTO = catalogMapper.toDto(catalog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCatalogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, catalogDTO.getIdCatalog())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Catalog in the database
        List<Catalog> catalogList = catalogRepository.findAll();
        assertThat(catalogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCatalog() throws Exception {
        int databaseSizeBeforeUpdate = catalogRepository.findAll().size();
        catalog.setIdCatalog(count.incrementAndGet());

        // Create the Catalog
        CatalogDTO catalogDTO = catalogMapper.toDto(catalog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Catalog in the database
        List<Catalog> catalogList = catalogRepository.findAll();
        assertThat(catalogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCatalog() throws Exception {
        int databaseSizeBeforeUpdate = catalogRepository.findAll().size();
        catalog.setIdCatalog(count.incrementAndGet());

        // Create the Catalog
        CatalogDTO catalogDTO = catalogMapper.toDto(catalog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(catalogDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Catalog in the database
        List<Catalog> catalogList = catalogRepository.findAll();
        assertThat(catalogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCatalogWithPatch() throws Exception {
        // Initialize the database
        catalogRepository.saveAndFlush(catalog);

        int databaseSizeBeforeUpdate = catalogRepository.findAll().size();

        // Update the catalog using partial update
        Catalog partialUpdatedCatalog = new Catalog();
        partialUpdatedCatalog.setIdCatalog(catalog.getIdCatalog());

        partialUpdatedCatalog.nota(UPDATED_NOTA);

        restCatalogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCatalog.getIdCatalog())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCatalog))
            )
            .andExpect(status().isOk());

        // Validate the Catalog in the database
        List<Catalog> catalogList = catalogRepository.findAll();
        assertThat(catalogList).hasSize(databaseSizeBeforeUpdate);
        Catalog testCatalog = catalogList.get(catalogList.size() - 1);
        assertThat(testCatalog.getNota()).isEqualTo(UPDATED_NOTA);
    }

    @Test
    @Transactional
    void fullUpdateCatalogWithPatch() throws Exception {
        // Initialize the database
        catalogRepository.saveAndFlush(catalog);

        int databaseSizeBeforeUpdate = catalogRepository.findAll().size();

        // Update the catalog using partial update
        Catalog partialUpdatedCatalog = new Catalog();
        partialUpdatedCatalog.setIdCatalog(catalog.getIdCatalog());

        partialUpdatedCatalog.nota(UPDATED_NOTA);

        restCatalogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCatalog.getIdCatalog())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCatalog))
            )
            .andExpect(status().isOk());

        // Validate the Catalog in the database
        List<Catalog> catalogList = catalogRepository.findAll();
        assertThat(catalogList).hasSize(databaseSizeBeforeUpdate);
        Catalog testCatalog = catalogList.get(catalogList.size() - 1);
        assertThat(testCatalog.getNota()).isEqualTo(UPDATED_NOTA);
    }

    @Test
    @Transactional
    void patchNonExistingCatalog() throws Exception {
        int databaseSizeBeforeUpdate = catalogRepository.findAll().size();
        catalog.setIdCatalog(count.incrementAndGet());

        // Create the Catalog
        CatalogDTO catalogDTO = catalogMapper.toDto(catalog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCatalogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, catalogDTO.getIdCatalog())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catalogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Catalog in the database
        List<Catalog> catalogList = catalogRepository.findAll();
        assertThat(catalogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCatalog() throws Exception {
        int databaseSizeBeforeUpdate = catalogRepository.findAll().size();
        catalog.setIdCatalog(count.incrementAndGet());

        // Create the Catalog
        CatalogDTO catalogDTO = catalogMapper.toDto(catalog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catalogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Catalog in the database
        List<Catalog> catalogList = catalogRepository.findAll();
        assertThat(catalogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCatalog() throws Exception {
        int databaseSizeBeforeUpdate = catalogRepository.findAll().size();
        catalog.setIdCatalog(count.incrementAndGet());

        // Create the Catalog
        CatalogDTO catalogDTO = catalogMapper.toDto(catalog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(catalogDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Catalog in the database
        List<Catalog> catalogList = catalogRepository.findAll();
        assertThat(catalogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCatalog() throws Exception {
        // Initialize the database
        catalogRepository.saveAndFlush(catalog);

        int databaseSizeBeforeDelete = catalogRepository.findAll().size();

        // Delete the catalog
        restCatalogMockMvc
            .perform(delete(ENTITY_API_URL_ID, catalog.getIdCatalog()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Catalog> catalogList = catalogRepository.findAll();
        assertThat(catalogList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
