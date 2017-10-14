package com.rfb.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.rfb.service.RfbUserService;
import com.rfb.service.dto.RfbUserDTO;
import com.rfb.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing RfbUser.
 */
@RestController
@RequestMapping("/api")
public class RfbUserResource {

    private final Logger log = LoggerFactory.getLogger(RfbUserResource.class);

    private static final String ENTITY_NAME = "rfbUser";

    private final RfbUserService rfbUserService;

    public RfbUserResource(RfbUserService rfbUserService) {
        this.rfbUserService = rfbUserService;
    }

    /**
     * POST  /rfb-users : Create a new rfbUser.
     *
     * @param rfbUserDTO the rfbUserDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new rfbUserDTO, or with status 400 (Bad Request) if the rfbUser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rfb-users")
    @Timed
    public ResponseEntity<RfbUserDTO> createRfbUser(@RequestBody RfbUserDTO rfbUserDTO) throws URISyntaxException {
        log.debug("REST request to save RfbUser : {}", rfbUserDTO);
        if (rfbUserDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new rfbUser cannot already have an ID")).body(null);
        }
        RfbUserDTO result = rfbUserService.save(rfbUserDTO);
        return ResponseEntity.created(new URI("/api/rfb-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rfb-users : Updates an existing rfbUser.
     *
     * @param rfbUserDTO the rfbUserDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated rfbUserDTO,
     * or with status 400 (Bad Request) if the rfbUserDTO is not valid,
     * or with status 500 (Internal Server Error) if the rfbUserDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/rfb-users")
    @Timed
    public ResponseEntity<RfbUserDTO> updateRfbUser(@RequestBody RfbUserDTO rfbUserDTO) throws URISyntaxException {
        log.debug("REST request to update RfbUser : {}", rfbUserDTO);
        if (rfbUserDTO.getId() == null) {
            return createRfbUser(rfbUserDTO);
        }
        RfbUserDTO result = rfbUserService.save(rfbUserDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, rfbUserDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rfb-users : get all the rfbUsers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of rfbUsers in body
     */
    @GetMapping("/rfb-users")
    @Timed
    public List<RfbUserDTO> getAllRfbUsers() {
        log.debug("REST request to get all RfbUsers");
        return rfbUserService.findAll();
        }

    /**
     * GET  /rfb-users/:id : get the "id" rfbUser.
     *
     * @param id the id of the rfbUserDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the rfbUserDTO, or with status 404 (Not Found)
     */
    @GetMapping("/rfb-users/{id}")
    @Timed
    public ResponseEntity<RfbUserDTO> getRfbUser(@PathVariable Long id) {
        log.debug("REST request to get RfbUser : {}", id);
        RfbUserDTO rfbUserDTO = rfbUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rfbUserDTO));
    }

    /**
     * DELETE  /rfb-users/:id : delete the "id" rfbUser.
     *
     * @param id the id of the rfbUserDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/rfb-users/{id}")
    @Timed
    public ResponseEntity<Void> deleteRfbUser(@PathVariable Long id) {
        log.debug("REST request to delete RfbUser : {}", id);
        rfbUserService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
