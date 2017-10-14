package com.rfb.service.impl;

import com.rfb.domain.RfbUser;
import com.rfb.repository.RfbUserRepository;
import com.rfb.service.RfbUserService;
import com.rfb.service.dto.RfbUserDTO;
import com.rfb.service.mapper.RfbUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing RfbUser.
 */
@Service
@Transactional
public class RfbUserServiceImpl implements RfbUserService{

    private final Logger log = LoggerFactory.getLogger(RfbUserServiceImpl.class);

    private final RfbUserRepository rfbUserRepository;

    private final RfbUserMapper rfbUserMapper;

    public RfbUserServiceImpl(RfbUserRepository rfbUserRepository, RfbUserMapper rfbUserMapper) {
        this.rfbUserRepository = rfbUserRepository;
        this.rfbUserMapper = rfbUserMapper;
    }

    /**
     * Save a rfbUser.
     *
     * @param rfbUserDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public RfbUserDTO save(RfbUserDTO rfbUserDTO) {
        log.debug("Request to save RfbUser : {}", rfbUserDTO);
        RfbUser rfbUser = rfbUserMapper.toEntity(rfbUserDTO);
        rfbUser = rfbUserRepository.save(rfbUser);
        return rfbUserMapper.toDto(rfbUser);
    }

    /**
     *  Get all the rfbUsers.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<RfbUserDTO> findAll() {
        log.debug("Request to get all RfbUsers");
        return rfbUserRepository.findAll().stream()
            .map(rfbUserMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one rfbUser by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public RfbUserDTO findOne(Long id) {
        log.debug("Request to get RfbUser : {}", id);
        RfbUser rfbUser = rfbUserRepository.findOne(id);
        return rfbUserMapper.toDto(rfbUser);
    }

    /**
     *  Delete the  rfbUser by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete RfbUser : {}", id);
        rfbUserRepository.delete(id);
    }
}
