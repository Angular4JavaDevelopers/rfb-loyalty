package com.rfb.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.rfb.service.RfbLocationService;
import com.rfb.service.dto.RfbLocationDTO;
import com.rfb.web.rest.util.HeaderUtil;
import com.rfb.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing RfbLocation.
 */
@RestController
@RequestMapping("/api")
public class RfbLocationResource {

    private final Logger log = LoggerFactory.getLogger(RfbLocationResource.class);

    private static final String ENTITY_NAME = "rfbLocation";

    private final RfbLocationService rfbLocationService;

    public RfbLocationResource(RfbLocationService rfbLocationService) {
        this.rfbLocationService = rfbLocationService;
    }

    /**
     * POST  /rfb-locations : Create a new rfbLocation.
     *
     * @param rfbLocationDTO the rfbLocationDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new rfbLocationDTO, or with status 400 (Bad Request) if the rfbLocation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rfb-locations")
    @Timed
    public ResponseEntity<RfbLocationDTO> createRfbLocation(@RequestBody RfbLocationDTO rfbLocationDTO) throws URISyntaxException {
        log.debug("REST request to save RfbLocation : {}", rfbLocationDTO);
        if (rfbLocationDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new rfbLocation cannot already have an ID")).body(null);
        }
        RfbLocationDTO result = rfbLocationService.save(rfbLocationDTO);
        return ResponseEntity.created(new URI("/api/rfb-locations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rfb-locations : Updates an existing rfbLocation.
     *
     * @param rfbLocationDTO the rfbLocationDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated rfbLocationDTO,
     * or with status 400 (Bad Request) if the rfbLocationDTO is not valid,
     * or with status 500 (Internal Server Error) if the rfbLocationDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/rfb-locations")
    @Timed
    public ResponseEntity<RfbLocationDTO> updateRfbLocation(@RequestBody RfbLocationDTO rfbLocationDTO) throws URISyntaxException {
        log.debug("REST request to update RfbLocation : {}", rfbLocationDTO);
        if (rfbLocationDTO.getId() == null) {
            return createRfbLocation(rfbLocationDTO);
        }
        RfbLocationDTO result = rfbLocationService.save(rfbLocationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, rfbLocationDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rfb-locations : get all the rfbLocations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of rfbLocations in body
     */
    @GetMapping("/rfb-locations")
    @Timed
    public ResponseEntity<List<RfbLocationDTO>> getAllRfbLocations(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of RfbLocations");
        Page<RfbLocationDTO> page = rfbLocationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/rfb-locations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /rfb-locations/:id : get the "id" rfbLocation.
     *
     * @param id the id of the rfbLocationDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the rfbLocationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/rfb-locations/{id}")
    @Timed
    public ResponseEntity<RfbLocationDTO> getRfbLocation(@PathVariable Long id) {
        log.debug("REST request to get RfbLocation : {}", id);
        RfbLocationDTO rfbLocationDTO = rfbLocationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rfbLocationDTO));
    }

    /**
     * DELETE  /rfb-locations/:id : delete the "id" rfbLocation.
     *
     * @param id the id of the rfbLocationDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/rfb-locations/{id}")
    @Timed
    public ResponseEntity<Void> deleteRfbLocation(@PathVariable Long id) {
        log.debug("REST request to delete RfbLocation : {}", id);
        rfbLocationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
