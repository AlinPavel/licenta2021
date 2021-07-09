package com.licenta.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.licenta.IntegrationTest;
import com.licenta.domain.Sport;
import com.licenta.repository.SportRepository;
import com.licenta.service.dto.SportDTO;
import com.licenta.service.mapper.SportMapper;
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
 * Integration tests for the {@link SportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SportResourceIT {

    private static final String DEFAULT_NUME = "AAAAAAAAAA";
    private static final String UPDATED_NUME = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATIE = "AAAAAAAAAA";
    private static final String UPDATED_LOCATIE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{idSport}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SportRepository sportRepository;

    @Autowired
    private SportMapper sportMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSportMockMvc;

    private Sport sport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sport createEntity(EntityManager em) {
        Sport sport = new Sport().nume(DEFAULT_NUME).locatie(DEFAULT_LOCATIE);
        return sport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sport createUpdatedEntity(EntityManager em) {
        Sport sport = new Sport().nume(UPDATED_NUME).locatie(UPDATED_LOCATIE);
        return sport;
    }

    @BeforeEach
    public void initTest() {
        sport = createEntity(em);
    }

    @Test
    @Transactional
    void createSport() throws Exception {
        int databaseSizeBeforeCreate = sportRepository.findAll().size();
        // Create the Sport
        SportDTO sportDTO = sportMapper.toDto(sport);
        restSportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sportDTO)))
            .andExpect(status().isCreated());

        // Validate the Sport in the database
        List<Sport> sportList = sportRepository.findAll();
        assertThat(sportList).hasSize(databaseSizeBeforeCreate + 1);
        Sport testSport = sportList.get(sportList.size() - 1);
        assertThat(testSport.getNume()).isEqualTo(DEFAULT_NUME);
        assertThat(testSport.getLocatie()).isEqualTo(DEFAULT_LOCATIE);
    }

    @Test
    @Transactional
    void createSportWithExistingId() throws Exception {
        // Create the Sport with an existing ID
        sport.setIdSport(1L);
        SportDTO sportDTO = sportMapper.toDto(sport);

        int databaseSizeBeforeCreate = sportRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sportDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Sport in the database
        List<Sport> sportList = sportRepository.findAll();
        assertThat(sportList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSports() throws Exception {
        // Initialize the database
        sportRepository.saveAndFlush(sport);

        // Get all the sportList
        restSportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=idSport,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].idSport").value(hasItem(sport.getIdSport().intValue())))
            .andExpect(jsonPath("$.[*].nume").value(hasItem(DEFAULT_NUME)))
            .andExpect(jsonPath("$.[*].locatie").value(hasItem(DEFAULT_LOCATIE)));
    }

    @Test
    @Transactional
    void getSport() throws Exception {
        // Initialize the database
        sportRepository.saveAndFlush(sport);

        // Get the sport
        restSportMockMvc
            .perform(get(ENTITY_API_URL_ID, sport.getIdSport()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.idSport").value(sport.getIdSport().intValue()))
            .andExpect(jsonPath("$.nume").value(DEFAULT_NUME))
            .andExpect(jsonPath("$.locatie").value(DEFAULT_LOCATIE));
    }

    @Test
    @Transactional
    void getNonExistingSport() throws Exception {
        // Get the sport
        restSportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSport() throws Exception {
        // Initialize the database
        sportRepository.saveAndFlush(sport);

        int databaseSizeBeforeUpdate = sportRepository.findAll().size();

        // Update the sport
        Sport updatedSport = sportRepository.findById(sport.getIdSport()).get();
        // Disconnect from session so that the updates on updatedSport are not directly saved in db
        em.detach(updatedSport);
        updatedSport.nume(UPDATED_NUME).locatie(UPDATED_LOCATIE);
        SportDTO sportDTO = sportMapper.toDto(updatedSport);

        restSportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sportDTO.getIdSport())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sportDTO))
            )
            .andExpect(status().isOk());

        // Validate the Sport in the database
        List<Sport> sportList = sportRepository.findAll();
        assertThat(sportList).hasSize(databaseSizeBeforeUpdate);
        Sport testSport = sportList.get(sportList.size() - 1);
        assertThat(testSport.getNume()).isEqualTo(UPDATED_NUME);
        assertThat(testSport.getLocatie()).isEqualTo(UPDATED_LOCATIE);
    }

    @Test
    @Transactional
    void putNonExistingSport() throws Exception {
        int databaseSizeBeforeUpdate = sportRepository.findAll().size();
        sport.setIdSport(count.incrementAndGet());

        // Create the Sport
        SportDTO sportDTO = sportMapper.toDto(sport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sportDTO.getIdSport())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sport in the database
        List<Sport> sportList = sportRepository.findAll();
        assertThat(sportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSport() throws Exception {
        int databaseSizeBeforeUpdate = sportRepository.findAll().size();
        sport.setIdSport(count.incrementAndGet());

        // Create the Sport
        SportDTO sportDTO = sportMapper.toDto(sport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sport in the database
        List<Sport> sportList = sportRepository.findAll();
        assertThat(sportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSport() throws Exception {
        int databaseSizeBeforeUpdate = sportRepository.findAll().size();
        sport.setIdSport(count.incrementAndGet());

        // Create the Sport
        SportDTO sportDTO = sportMapper.toDto(sport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSportMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sportDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sport in the database
        List<Sport> sportList = sportRepository.findAll();
        assertThat(sportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSportWithPatch() throws Exception {
        // Initialize the database
        sportRepository.saveAndFlush(sport);

        int databaseSizeBeforeUpdate = sportRepository.findAll().size();

        // Update the sport using partial update
        Sport partialUpdatedSport = new Sport();
        partialUpdatedSport.setIdSport(sport.getIdSport());

        partialUpdatedSport.nume(UPDATED_NUME);

        restSportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSport.getIdSport())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSport))
            )
            .andExpect(status().isOk());

        // Validate the Sport in the database
        List<Sport> sportList = sportRepository.findAll();
        assertThat(sportList).hasSize(databaseSizeBeforeUpdate);
        Sport testSport = sportList.get(sportList.size() - 1);
        assertThat(testSport.getNume()).isEqualTo(UPDATED_NUME);
        assertThat(testSport.getLocatie()).isEqualTo(DEFAULT_LOCATIE);
    }

    @Test
    @Transactional
    void fullUpdateSportWithPatch() throws Exception {
        // Initialize the database
        sportRepository.saveAndFlush(sport);

        int databaseSizeBeforeUpdate = sportRepository.findAll().size();

        // Update the sport using partial update
        Sport partialUpdatedSport = new Sport();
        partialUpdatedSport.setIdSport(sport.getIdSport());

        partialUpdatedSport.nume(UPDATED_NUME).locatie(UPDATED_LOCATIE);

        restSportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSport.getIdSport())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSport))
            )
            .andExpect(status().isOk());

        // Validate the Sport in the database
        List<Sport> sportList = sportRepository.findAll();
        assertThat(sportList).hasSize(databaseSizeBeforeUpdate);
        Sport testSport = sportList.get(sportList.size() - 1);
        assertThat(testSport.getNume()).isEqualTo(UPDATED_NUME);
        assertThat(testSport.getLocatie()).isEqualTo(UPDATED_LOCATIE);
    }

    @Test
    @Transactional
    void patchNonExistingSport() throws Exception {
        int databaseSizeBeforeUpdate = sportRepository.findAll().size();
        sport.setIdSport(count.incrementAndGet());

        // Create the Sport
        SportDTO sportDTO = sportMapper.toDto(sport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sportDTO.getIdSport())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sport in the database
        List<Sport> sportList = sportRepository.findAll();
        assertThat(sportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSport() throws Exception {
        int databaseSizeBeforeUpdate = sportRepository.findAll().size();
        sport.setIdSport(count.incrementAndGet());

        // Create the Sport
        SportDTO sportDTO = sportMapper.toDto(sport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sport in the database
        List<Sport> sportList = sportRepository.findAll();
        assertThat(sportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSport() throws Exception {
        int databaseSizeBeforeUpdate = sportRepository.findAll().size();
        sport.setIdSport(count.incrementAndGet());

        // Create the Sport
        SportDTO sportDTO = sportMapper.toDto(sport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSportMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sportDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sport in the database
        List<Sport> sportList = sportRepository.findAll();
        assertThat(sportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSport() throws Exception {
        // Initialize the database
        sportRepository.saveAndFlush(sport);

        int databaseSizeBeforeDelete = sportRepository.findAll().size();

        // Delete the sport
        restSportMockMvc
            .perform(delete(ENTITY_API_URL_ID, sport.getIdSport()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Sport> sportList = sportRepository.findAll();
        assertThat(sportList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
