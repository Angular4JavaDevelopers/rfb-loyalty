package com.rfb.service;

import com.rfb.service.dto.RfbUserDTO;

import java.util.List;

/**
 * Service Interface for managing RfbUser.
 */
public interface RfbUserService {

    /**
     * Save a rfbUser.
     *
     * @param rfbUserDTO the entity to save
     * @return the persisted entity
     */
    RfbUserDTO save(RfbUserDTO rfbUserDTO);

    /**
     *  Get all the rfbUsers.
     *
     *  @return the list of entities
     */
    List<RfbUserDTO> findAll();

    /**
     *  Get the "id" rfbUser.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    RfbUserDTO findOne(Long id);

    /**
     *  Delete the "id" rfbUser.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
