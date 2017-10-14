package com.rfb.web.rest;

import com.rfb.RfbloyaltyApp;
import com.rfb.domain.RfbLocation;
import com.rfb.repository.RfbLocationRepository;
import com.rfb.service.RfbLocationService;
import com.rfb.service.dto.RfbLocationDTO;
import com.rfb.service.mapper.RfbLocationMapper;
import com.rfb.web.rest.errors.ExceptionTranslator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RfbLocationResource REST controller.
 *
 * @see RfbLocationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RfbloyaltyApp.class)
public class RfbLocationResourceIntTest {

    private static final String DEFAULT_LOCATION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_RUN_DAY_OF_WEEK = 1;
    private static final Integer UPDATED_RUN_DAY_OF_WEEK = 2;

    @Autowired
    private RfbLocationRepository rfbLocationRepository;

    @Autowired
    private RfbLocationMapper rfbLocationMapper;

    @Autowired
    private RfbLocationService rfbLocationService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRfbLocationMockMvc;

    private RfbLocation rfbLocation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RfbLocationResource rfbLocationResource = new RfbLocationResource(rfbLocationService);
        this.restRfbLocationMockMvc = MockMvcBuilders.standaloneSetup(rfbLocationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RfbLocation createEntity(EntityManager em) {
        RfbLocation rfbLocation = new RfbLocation()
            .locationName(DEFAULT_LOCATION_NAME)
            .runDayOfWeek(DEFAULT_RUN_DAY_OF_WEEK);
        return rfbLocation;
    }

    @Before
    public void initTest() {
        rfbLocation = createEntity(em);
    }

    @Test
    @Transactional
    public void createRfbLocation() throws Exception {
        int databaseSizeBeforeCreate = rfbLocationRepository.findAll().size();

        // Create the RfbLocation
        RfbLocationDTO rfbLocationDTO = rfbLocationMapper.toDto(rfbLocation);
        restRfbLocationMockMvc.perform(post("/api/rfb-locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rfbLocationDTO)))
            .andExpect(status().isCreated());

        // Validate the RfbLocation in the database
        List<RfbLocation> rfbLocationList = rfbLocationRepository.findAll();
        assertThat(rfbLocationList).hasSize(databaseSizeBeforeCreate + 1);
        RfbLocation testRfbLocation = rfbLocationList.get(rfbLocationList.size() - 1);
        assertThat(testRfbLocation.getLocationName()).isEqualTo(DEFAULT_LOCATION_NAME);
        assertThat(testRfbLocation.getRunDayOfWeek()).isEqualTo(DEFAULT_RUN_DAY_OF_WEEK);
    }

    @Test
    @Transactional
    public void createRfbLocationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rfbLocationRepository.findAll().size();

        // Create the RfbLocation with an existing ID
        rfbLocation.setId(1L);
        RfbLocationDTO rfbLocationDTO = rfbLocationMapper.toDto(rfbLocation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRfbLocationMockMvc.perform(post("/api/rfb-locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rfbLocationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RfbLocation in the database
        List<RfbLocation> rfbLocationList = rfbLocationRepository.findAll();
        assertThat(rfbLocationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRfbLocations() throws Exception {
        // Initialize the database
        rfbLocationRepository.saveAndFlush(rfbLocation);

        // Get all the rfbLocationList
        restRfbLocationMockMvc.perform(get("/api/rfb-locations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rfbLocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].locationName").value(hasItem(DEFAULT_LOCATION_NAME.toString())))
            .andExpect(jsonPath("$.[*].runDayOfWeek").value(hasItem(DEFAULT_RUN_DAY_OF_WEEK)));
    }

    @Test
    @Transactional
    public void getRfbLocation() throws Exception {
        // Initialize the database
        rfbLocationRepository.saveAndFlush(rfbLocation);

        // Get the rfbLocation
        restRfbLocationMockMvc.perform(get("/api/rfb-locations/{id}", rfbLocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rfbLocation.getId().intValue()))
            .andExpect(jsonPath("$.locationName").value(DEFAULT_LOCATION_NAME.toString()))
            .andExpect(jsonPath("$.runDayOfWeek").value(DEFAULT_RUN_DAY_OF_WEEK));
    }

    @Test
    @Transactional
    public void getNonExistingRfbLocation() throws Exception {
        // Get the rfbLocation
        restRfbLocationMockMvc.perform(get("/api/rfb-locations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRfbLocation() throws Exception {
        // Initialize the database
        rfbLocationRepository.saveAndFlush(rfbLocation);
        int databaseSizeBeforeUpdate = rfbLocationRepository.findAll().size();

        // Update the rfbLocation
        RfbLocation updatedRfbLocation = rfbLocationRepository.findOne(rfbLocation.getId());
        updatedRfbLocation
            .locationName(UPDATED_LOCATION_NAME)
            .runDayOfWeek(UPDATED_RUN_DAY_OF_WEEK);
        RfbLocationDTO rfbLocationDTO = rfbLocationMapper.toDto(updatedRfbLocation);

        restRfbLocationMockMvc.perform(put("/api/rfb-locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rfbLocationDTO)))
            .andExpect(status().isOk());

        // Validate the RfbLocation in the database
        List<RfbLocation> rfbLocationList = rfbLocationRepository.findAll();
        assertThat(rfbLocationList).hasSize(databaseSizeBeforeUpdate);
        RfbLocation testRfbLocation = rfbLocationList.get(rfbLocationList.size() - 1);
        assertThat(testRfbLocation.getLocationName()).isEqualTo(UPDATED_LOCATION_NAME);
        assertThat(testRfbLocation.getRunDayOfWeek()).isEqualTo(UPDATED_RUN_DAY_OF_WEEK);
    }

    @Test
    @Transactional
    public void updateNonExistingRfbLocation() throws Exception {
        int databaseSizeBeforeUpdate = rfbLocationRepository.findAll().size();

        // Create the RfbLocation
        RfbLocationDTO rfbLocationDTO = rfbLocationMapper.toDto(rfbLocation);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRfbLocationMockMvc.perform(put("/api/rfb-locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rfbLocationDTO)))
            .andExpect(status().isCreated());

        // Validate the RfbLocation in the database
        List<RfbLocation> rfbLocationList = rfbLocationRepository.findAll();
        assertThat(rfbLocationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRfbLocation() throws Exception {
        // Initialize the database
        rfbLocationRepository.saveAndFlush(rfbLocation);
        int databaseSizeBeforeDelete = rfbLocationRepository.findAll().size();

        // Get the rfbLocation
        restRfbLocationMockMvc.perform(delete("/api/rfb-locations/{id}", rfbLocation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RfbLocation> rfbLocationList = rfbLocationRepository.findAll();
        assertThat(rfbLocationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RfbLocation.class);
        RfbLocation rfbLocation1 = new RfbLocation();
        rfbLocation1.setId(1L);
        RfbLocation rfbLocation2 = new RfbLocation();
        rfbLocation2.setId(rfbLocation1.getId());
        assertThat(rfbLocation1).isEqualTo(rfbLocation2);
        rfbLocation2.setId(2L);
        assertThat(rfbLocation1).isNotEqualTo(rfbLocation2);
        rfbLocation1.setId(null);
        assertThat(rfbLocation1).isNotEqualTo(rfbLocation2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RfbLocationDTO.class);
        RfbLocationDTO rfbLocationDTO1 = new RfbLocationDTO();
        rfbLocationDTO1.setId(1L);
        RfbLocationDTO rfbLocationDTO2 = new RfbLocationDTO();
        assertThat(rfbLocationDTO1).isNotEqualTo(rfbLocationDTO2);
        rfbLocationDTO2.setId(rfbLocationDTO1.getId());
        assertThat(rfbLocationDTO1).isEqualTo(rfbLocationDTO2);
        rfbLocationDTO2.setId(2L);
        assertThat(rfbLocationDTO1).isNotEqualTo(rfbLocationDTO2);
        rfbLocationDTO1.setId(null);
        assertThat(rfbLocationDTO1).isNotEqualTo(rfbLocationDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(rfbLocationMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(rfbLocationMapper.fromId(null)).isNull();
    }
}
