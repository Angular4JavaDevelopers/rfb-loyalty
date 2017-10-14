package com.rfb.web.rest;

import com.rfb.RfbloyaltyApp;
import com.rfb.domain.RfbUser;
import com.rfb.repository.RfbUserRepository;
import com.rfb.service.RfbUserService;
import com.rfb.service.dto.RfbUserDTO;
import com.rfb.service.mapper.RfbUserMapper;
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
 * Test class for the RfbUserResource REST controller.
 *
 * @see RfbUserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RfbloyaltyApp.class)
public class RfbUserResourceIntTest {

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    @Autowired
    private RfbUserRepository rfbUserRepository;

    @Autowired
    private RfbUserMapper rfbUserMapper;

    @Autowired
    private RfbUserService rfbUserService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRfbUserMockMvc;

    private RfbUser rfbUser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RfbUserResource rfbUserResource = new RfbUserResource(rfbUserService);
        this.restRfbUserMockMvc = MockMvcBuilders.standaloneSetup(rfbUserResource)
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
    public static RfbUser createEntity(EntityManager em) {
        RfbUser rfbUser = new RfbUser()
            .username(DEFAULT_USERNAME);
        return rfbUser;
    }

    @Before
    public void initTest() {
        rfbUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createRfbUser() throws Exception {
        int databaseSizeBeforeCreate = rfbUserRepository.findAll().size();

        // Create the RfbUser
        RfbUserDTO rfbUserDTO = rfbUserMapper.toDto(rfbUser);
        restRfbUserMockMvc.perform(post("/api/rfb-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rfbUserDTO)))
            .andExpect(status().isCreated());

        // Validate the RfbUser in the database
        List<RfbUser> rfbUserList = rfbUserRepository.findAll();
        assertThat(rfbUserList).hasSize(databaseSizeBeforeCreate + 1);
        RfbUser testRfbUser = rfbUserList.get(rfbUserList.size() - 1);
        assertThat(testRfbUser.getUsername()).isEqualTo(DEFAULT_USERNAME);
    }

    @Test
    @Transactional
    public void createRfbUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rfbUserRepository.findAll().size();

        // Create the RfbUser with an existing ID
        rfbUser.setId(1L);
        RfbUserDTO rfbUserDTO = rfbUserMapper.toDto(rfbUser);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRfbUserMockMvc.perform(post("/api/rfb-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rfbUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RfbUser in the database
        List<RfbUser> rfbUserList = rfbUserRepository.findAll();
        assertThat(rfbUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRfbUsers() throws Exception {
        // Initialize the database
        rfbUserRepository.saveAndFlush(rfbUser);

        // Get all the rfbUserList
        restRfbUserMockMvc.perform(get("/api/rfb-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rfbUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())));
    }

    @Test
    @Transactional
    public void getRfbUser() throws Exception {
        // Initialize the database
        rfbUserRepository.saveAndFlush(rfbUser);

        // Get the rfbUser
        restRfbUserMockMvc.perform(get("/api/rfb-users/{id}", rfbUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rfbUser.getId().intValue()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRfbUser() throws Exception {
        // Get the rfbUser
        restRfbUserMockMvc.perform(get("/api/rfb-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRfbUser() throws Exception {
        // Initialize the database
        rfbUserRepository.saveAndFlush(rfbUser);
        int databaseSizeBeforeUpdate = rfbUserRepository.findAll().size();

        // Update the rfbUser
        RfbUser updatedRfbUser = rfbUserRepository.findOne(rfbUser.getId());
        updatedRfbUser
            .username(UPDATED_USERNAME);
        RfbUserDTO rfbUserDTO = rfbUserMapper.toDto(updatedRfbUser);

        restRfbUserMockMvc.perform(put("/api/rfb-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rfbUserDTO)))
            .andExpect(status().isOk());

        // Validate the RfbUser in the database
        List<RfbUser> rfbUserList = rfbUserRepository.findAll();
        assertThat(rfbUserList).hasSize(databaseSizeBeforeUpdate);
        RfbUser testRfbUser = rfbUserList.get(rfbUserList.size() - 1);
        assertThat(testRfbUser.getUsername()).isEqualTo(UPDATED_USERNAME);
    }

    @Test
    @Transactional
    public void updateNonExistingRfbUser() throws Exception {
        int databaseSizeBeforeUpdate = rfbUserRepository.findAll().size();

        // Create the RfbUser
        RfbUserDTO rfbUserDTO = rfbUserMapper.toDto(rfbUser);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRfbUserMockMvc.perform(put("/api/rfb-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rfbUserDTO)))
            .andExpect(status().isCreated());

        // Validate the RfbUser in the database
        List<RfbUser> rfbUserList = rfbUserRepository.findAll();
        assertThat(rfbUserList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRfbUser() throws Exception {
        // Initialize the database
        rfbUserRepository.saveAndFlush(rfbUser);
        int databaseSizeBeforeDelete = rfbUserRepository.findAll().size();

        // Get the rfbUser
        restRfbUserMockMvc.perform(delete("/api/rfb-users/{id}", rfbUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RfbUser> rfbUserList = rfbUserRepository.findAll();
        assertThat(rfbUserList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RfbUser.class);
        RfbUser rfbUser1 = new RfbUser();
        rfbUser1.setId(1L);
        RfbUser rfbUser2 = new RfbUser();
        rfbUser2.setId(rfbUser1.getId());
        assertThat(rfbUser1).isEqualTo(rfbUser2);
        rfbUser2.setId(2L);
        assertThat(rfbUser1).isNotEqualTo(rfbUser2);
        rfbUser1.setId(null);
        assertThat(rfbUser1).isNotEqualTo(rfbUser2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RfbUserDTO.class);
        RfbUserDTO rfbUserDTO1 = new RfbUserDTO();
        rfbUserDTO1.setId(1L);
        RfbUserDTO rfbUserDTO2 = new RfbUserDTO();
        assertThat(rfbUserDTO1).isNotEqualTo(rfbUserDTO2);
        rfbUserDTO2.setId(rfbUserDTO1.getId());
        assertThat(rfbUserDTO1).isEqualTo(rfbUserDTO2);
        rfbUserDTO2.setId(2L);
        assertThat(rfbUserDTO1).isNotEqualTo(rfbUserDTO2);
        rfbUserDTO1.setId(null);
        assertThat(rfbUserDTO1).isNotEqualTo(rfbUserDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(rfbUserMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(rfbUserMapper.fromId(null)).isNull();
    }
}
