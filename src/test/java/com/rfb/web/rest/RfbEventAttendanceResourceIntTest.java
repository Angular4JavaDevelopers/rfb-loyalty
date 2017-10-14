package com.rfb.web.rest;

import com.rfb.RfbloyaltyApp;
import com.rfb.domain.RfbEventAttendance;
import com.rfb.repository.RfbEventAttendanceRepository;
import com.rfb.service.RfbEventAttendanceService;
import com.rfb.service.dto.RfbEventAttendanceDTO;
import com.rfb.service.mapper.RfbEventAttendanceMapper;
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
 * Test class for the RfbEventAttendanceResource REST controller.
 *
 * @see RfbEventAttendanceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RfbloyaltyApp.class)
public class RfbEventAttendanceResourceIntTest {

    private static final LocalDate DEFAULT_ATTENDANCE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ATTENDANCE_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private RfbEventAttendanceRepository rfbEventAttendanceRepository;

    @Autowired
    private RfbEventAttendanceMapper rfbEventAttendanceMapper;

    @Autowired
    private RfbEventAttendanceService rfbEventAttendanceService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRfbEventAttendanceMockMvc;

    private RfbEventAttendance rfbEventAttendance;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RfbEventAttendanceResource rfbEventAttendanceResource = new RfbEventAttendanceResource(rfbEventAttendanceService);
        this.restRfbEventAttendanceMockMvc = MockMvcBuilders.standaloneSetup(rfbEventAttendanceResource)
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
    public static RfbEventAttendance createEntity(EntityManager em) {
        RfbEventAttendance rfbEventAttendance = new RfbEventAttendance()
            .attendanceDate(DEFAULT_ATTENDANCE_DATE);
        return rfbEventAttendance;
    }

    @Before
    public void initTest() {
        rfbEventAttendance = createEntity(em);
    }

    @Test
    @Transactional
    public void createRfbEventAttendance() throws Exception {
        int databaseSizeBeforeCreate = rfbEventAttendanceRepository.findAll().size();

        // Create the RfbEventAttendance
        RfbEventAttendanceDTO rfbEventAttendanceDTO = rfbEventAttendanceMapper.toDto(rfbEventAttendance);
        restRfbEventAttendanceMockMvc.perform(post("/api/rfb-event-attendances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rfbEventAttendanceDTO)))
            .andExpect(status().isCreated());

        // Validate the RfbEventAttendance in the database
        List<RfbEventAttendance> rfbEventAttendanceList = rfbEventAttendanceRepository.findAll();
        assertThat(rfbEventAttendanceList).hasSize(databaseSizeBeforeCreate + 1);
        RfbEventAttendance testRfbEventAttendance = rfbEventAttendanceList.get(rfbEventAttendanceList.size() - 1);
        assertThat(testRfbEventAttendance.getAttendanceDate()).isEqualTo(DEFAULT_ATTENDANCE_DATE);
    }

    @Test
    @Transactional
    public void createRfbEventAttendanceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rfbEventAttendanceRepository.findAll().size();

        // Create the RfbEventAttendance with an existing ID
        rfbEventAttendance.setId(1L);
        RfbEventAttendanceDTO rfbEventAttendanceDTO = rfbEventAttendanceMapper.toDto(rfbEventAttendance);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRfbEventAttendanceMockMvc.perform(post("/api/rfb-event-attendances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rfbEventAttendanceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RfbEventAttendance in the database
        List<RfbEventAttendance> rfbEventAttendanceList = rfbEventAttendanceRepository.findAll();
        assertThat(rfbEventAttendanceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRfbEventAttendances() throws Exception {
        // Initialize the database
        rfbEventAttendanceRepository.saveAndFlush(rfbEventAttendance);

        // Get all the rfbEventAttendanceList
        restRfbEventAttendanceMockMvc.perform(get("/api/rfb-event-attendances?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rfbEventAttendance.getId().intValue())))
            .andExpect(jsonPath("$.[*].attendanceDate").value(hasItem(DEFAULT_ATTENDANCE_DATE.toString())));
    }

    @Test
    @Transactional
    public void getRfbEventAttendance() throws Exception {
        // Initialize the database
        rfbEventAttendanceRepository.saveAndFlush(rfbEventAttendance);

        // Get the rfbEventAttendance
        restRfbEventAttendanceMockMvc.perform(get("/api/rfb-event-attendances/{id}", rfbEventAttendance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rfbEventAttendance.getId().intValue()))
            .andExpect(jsonPath("$.attendanceDate").value(DEFAULT_ATTENDANCE_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRfbEventAttendance() throws Exception {
        // Get the rfbEventAttendance
        restRfbEventAttendanceMockMvc.perform(get("/api/rfb-event-attendances/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRfbEventAttendance() throws Exception {
        // Initialize the database
        rfbEventAttendanceRepository.saveAndFlush(rfbEventAttendance);
        int databaseSizeBeforeUpdate = rfbEventAttendanceRepository.findAll().size();

        // Update the rfbEventAttendance
        RfbEventAttendance updatedRfbEventAttendance = rfbEventAttendanceRepository.findOne(rfbEventAttendance.getId());
        updatedRfbEventAttendance
            .attendanceDate(UPDATED_ATTENDANCE_DATE);
        RfbEventAttendanceDTO rfbEventAttendanceDTO = rfbEventAttendanceMapper.toDto(updatedRfbEventAttendance);

        restRfbEventAttendanceMockMvc.perform(put("/api/rfb-event-attendances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rfbEventAttendanceDTO)))
            .andExpect(status().isOk());

        // Validate the RfbEventAttendance in the database
        List<RfbEventAttendance> rfbEventAttendanceList = rfbEventAttendanceRepository.findAll();
        assertThat(rfbEventAttendanceList).hasSize(databaseSizeBeforeUpdate);
        RfbEventAttendance testRfbEventAttendance = rfbEventAttendanceList.get(rfbEventAttendanceList.size() - 1);
        assertThat(testRfbEventAttendance.getAttendanceDate()).isEqualTo(UPDATED_ATTENDANCE_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingRfbEventAttendance() throws Exception {
        int databaseSizeBeforeUpdate = rfbEventAttendanceRepository.findAll().size();

        // Create the RfbEventAttendance
        RfbEventAttendanceDTO rfbEventAttendanceDTO = rfbEventAttendanceMapper.toDto(rfbEventAttendance);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRfbEventAttendanceMockMvc.perform(put("/api/rfb-event-attendances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rfbEventAttendanceDTO)))
            .andExpect(status().isCreated());

        // Validate the RfbEventAttendance in the database
        List<RfbEventAttendance> rfbEventAttendanceList = rfbEventAttendanceRepository.findAll();
        assertThat(rfbEventAttendanceList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRfbEventAttendance() throws Exception {
        // Initialize the database
        rfbEventAttendanceRepository.saveAndFlush(rfbEventAttendance);
        int databaseSizeBeforeDelete = rfbEventAttendanceRepository.findAll().size();

        // Get the rfbEventAttendance
        restRfbEventAttendanceMockMvc.perform(delete("/api/rfb-event-attendances/{id}", rfbEventAttendance.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RfbEventAttendance> rfbEventAttendanceList = rfbEventAttendanceRepository.findAll();
        assertThat(rfbEventAttendanceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RfbEventAttendance.class);
        RfbEventAttendance rfbEventAttendance1 = new RfbEventAttendance();
        rfbEventAttendance1.setId(1L);
        RfbEventAttendance rfbEventAttendance2 = new RfbEventAttendance();
        rfbEventAttendance2.setId(rfbEventAttendance1.getId());
        assertThat(rfbEventAttendance1).isEqualTo(rfbEventAttendance2);
        rfbEventAttendance2.setId(2L);
        assertThat(rfbEventAttendance1).isNotEqualTo(rfbEventAttendance2);
        rfbEventAttendance1.setId(null);
        assertThat(rfbEventAttendance1).isNotEqualTo(rfbEventAttendance2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RfbEventAttendanceDTO.class);
        RfbEventAttendanceDTO rfbEventAttendanceDTO1 = new RfbEventAttendanceDTO();
        rfbEventAttendanceDTO1.setId(1L);
        RfbEventAttendanceDTO rfbEventAttendanceDTO2 = new RfbEventAttendanceDTO();
        assertThat(rfbEventAttendanceDTO1).isNotEqualTo(rfbEventAttendanceDTO2);
        rfbEventAttendanceDTO2.setId(rfbEventAttendanceDTO1.getId());
        assertThat(rfbEventAttendanceDTO1).isEqualTo(rfbEventAttendanceDTO2);
        rfbEventAttendanceDTO2.setId(2L);
        assertThat(rfbEventAttendanceDTO1).isNotEqualTo(rfbEventAttendanceDTO2);
        rfbEventAttendanceDTO1.setId(null);
        assertThat(rfbEventAttendanceDTO1).isNotEqualTo(rfbEventAttendanceDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(rfbEventAttendanceMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(rfbEventAttendanceMapper.fromId(null)).isNull();
    }
}
