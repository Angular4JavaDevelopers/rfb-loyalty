package com.rfb.web.rest;

import com.rfb.RfbloyaltyApp;
import com.rfb.domain.RfbEvent;
import com.rfb.repository.RfbEventRepository;
import com.rfb.service.RfbEventService;
import com.rfb.service.dto.RfbEventDTO;
import com.rfb.service.mapper.RfbEventMapper;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RfbEventResource REST controller.
 *
 * @see RfbEventResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RfbloyaltyApp.class)
public class RfbEventResourceIntTest {

    private static final LocalDate DEFAULT_EVENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EVENT_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_EVENT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_CODE = "BBBBBBBBBB";

    @Autowired
    private RfbEventRepository rfbEventRepository;

    @Autowired
    private RfbEventMapper rfbEventMapper;

    @Autowired
    private RfbEventService rfbEventService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRfbEventMockMvc;

    private RfbEvent rfbEvent;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RfbEventResource rfbEventResource = new RfbEventResource(rfbEventService);
        this.restRfbEventMockMvc = MockMvcBuilders.standaloneSetup(rfbEventResource)
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
    public static RfbEvent createEntity(EntityManager em) {
        RfbEvent rfbEvent = new RfbEvent()
            .eventDate(DEFAULT_EVENT_DATE)
            .eventCode(DEFAULT_EVENT_CODE);
        return rfbEvent;
    }

    @Before
    public void initTest() {
        rfbEvent = createEntity(em);
    }

    @Test
    @Transactional
    public void createRfbEvent() throws Exception {
        int databaseSizeBeforeCreate = rfbEventRepository.findAll().size();

        // Create the RfbEvent
        RfbEventDTO rfbEventDTO = rfbEventMapper.toDto(rfbEvent);
        restRfbEventMockMvc.perform(post("/api/rfb-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rfbEventDTO)))
            .andExpect(status().isCreated());

        // Validate the RfbEvent in the database
        List<RfbEvent> rfbEventList = rfbEventRepository.findAll();
        assertThat(rfbEventList).hasSize(databaseSizeBeforeCreate + 1);
        RfbEvent testRfbEvent = rfbEventList.get(rfbEventList.size() - 1);
        assertThat(testRfbEvent.getEventDate()).isEqualTo(DEFAULT_EVENT_DATE);
        assertThat(testRfbEvent.getEventCode()).isEqualTo(DEFAULT_EVENT_CODE);
    }

    @Test
    @Transactional
    public void createRfbEventWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rfbEventRepository.findAll().size();

        // Create the RfbEvent with an existing ID
        rfbEvent.setId(1L);
        RfbEventDTO rfbEventDTO = rfbEventMapper.toDto(rfbEvent);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRfbEventMockMvc.perform(post("/api/rfb-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rfbEventDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RfbEvent in the database
        List<RfbEvent> rfbEventList = rfbEventRepository.findAll();
        assertThat(rfbEventList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRfbEvents() throws Exception {
        // Initialize the database
        rfbEventRepository.saveAndFlush(rfbEvent);

        // Get all the rfbEventList
        restRfbEventMockMvc.perform(get("/api/rfb-events?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rfbEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventDate").value(hasItem(DEFAULT_EVENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].eventCode").value(hasItem(DEFAULT_EVENT_CODE.toString())));
    }

    @Test
    @Transactional
    public void getRfbEvent() throws Exception {
        // Initialize the database
        rfbEventRepository.saveAndFlush(rfbEvent);

        // Get the rfbEvent
        restRfbEventMockMvc.perform(get("/api/rfb-events/{id}", rfbEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rfbEvent.getId().intValue()))
            .andExpect(jsonPath("$.eventDate").value(DEFAULT_EVENT_DATE.toString()))
            .andExpect(jsonPath("$.eventCode").value(DEFAULT_EVENT_CODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRfbEvent() throws Exception {
        // Get the rfbEvent
        restRfbEventMockMvc.perform(get("/api/rfb-events/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRfbEvent() throws Exception {
        // Initialize the database
        rfbEventRepository.saveAndFlush(rfbEvent);
        int databaseSizeBeforeUpdate = rfbEventRepository.findAll().size();

        // Update the rfbEvent
        RfbEvent updatedRfbEvent = rfbEventRepository.findOne(rfbEvent.getId());
        updatedRfbEvent
            .eventDate(UPDATED_EVENT_DATE)
            .eventCode(UPDATED_EVENT_CODE);
        RfbEventDTO rfbEventDTO = rfbEventMapper.toDto(updatedRfbEvent);

        restRfbEventMockMvc.perform(put("/api/rfb-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rfbEventDTO)))
            .andExpect(status().isOk());

        // Validate the RfbEvent in the database
        List<RfbEvent> rfbEventList = rfbEventRepository.findAll();
        assertThat(rfbEventList).hasSize(databaseSizeBeforeUpdate);
        RfbEvent testRfbEvent = rfbEventList.get(rfbEventList.size() - 1);
        assertThat(testRfbEvent.getEventDate()).isEqualTo(UPDATED_EVENT_DATE);
        assertThat(testRfbEvent.getEventCode()).isEqualTo(UPDATED_EVENT_CODE);
    }

    @Test
    @Transactional
    public void updateNonExistingRfbEvent() throws Exception {
        int databaseSizeBeforeUpdate = rfbEventRepository.findAll().size();

        // Create the RfbEvent
        RfbEventDTO rfbEventDTO = rfbEventMapper.toDto(rfbEvent);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRfbEventMockMvc.perform(put("/api/rfb-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rfbEventDTO)))
            .andExpect(status().isCreated());

        // Validate the RfbEvent in the database
        List<RfbEvent> rfbEventList = rfbEventRepository.findAll();
        assertThat(rfbEventList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRfbEvent() throws Exception {
        // Initialize the database
        rfbEventRepository.saveAndFlush(rfbEvent);
        int databaseSizeBeforeDelete = rfbEventRepository.findAll().size();

        // Get the rfbEvent
        restRfbEventMockMvc.perform(delete("/api/rfb-events/{id}", rfbEvent.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RfbEvent> rfbEventList = rfbEventRepository.findAll();
        assertThat(rfbEventList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RfbEvent.class);
        RfbEvent rfbEvent1 = new RfbEvent();
        rfbEvent1.setId(1L);
        RfbEvent rfbEvent2 = new RfbEvent();
        rfbEvent2.setId(rfbEvent1.getId());
        assertThat(rfbEvent1).isEqualTo(rfbEvent2);
        rfbEvent2.setId(2L);
        assertThat(rfbEvent1).isNotEqualTo(rfbEvent2);
        rfbEvent1.setId(null);
        assertThat(rfbEvent1).isNotEqualTo(rfbEvent2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RfbEventDTO.class);
        RfbEventDTO rfbEventDTO1 = new RfbEventDTO();
        rfbEventDTO1.setId(1L);
        RfbEventDTO rfbEventDTO2 = new RfbEventDTO();
        assertThat(rfbEventDTO1).isNotEqualTo(rfbEventDTO2);
        rfbEventDTO2.setId(rfbEventDTO1.getId());
        assertThat(rfbEventDTO1).isEqualTo(rfbEventDTO2);
        rfbEventDTO2.setId(2L);
        assertThat(rfbEventDTO1).isNotEqualTo(rfbEventDTO2);
        rfbEventDTO1.setId(null);
        assertThat(rfbEventDTO1).isNotEqualTo(rfbEventDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(rfbEventMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(rfbEventMapper.fromId(null)).isNull();
    }
}
