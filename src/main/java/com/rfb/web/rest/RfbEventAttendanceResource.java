package com.rfb.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.rfb.service.RfbEventAttendanceService;
import com.rfb.service.dto.RfbEventAttendanceDTO;
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
 * REST controller for managing RfbEventAttendance.
 */
@RestController
@RequestMapping("/api")
public class RfbEventAttendanceResource {

    private final Logger log = LoggerFactory.getLogger(RfbEventAttendanceResource.class);

    private static final String ENTITY_NAME = "rfbEventAttendance";

    private final RfbEventAttendanceService rfbEventAttendanceService;

    public RfbEventAttendanceResource(RfbEventAttendanceService rfbEventAttendanceService) {
        this.rfbEventAttendanceService = rfbEventAttendanceService;
    }

    /**
     * POST  /rfb-event-attendances : Create a new rfbEventAttendance.
     *
     * @param rfbEventAttendanceDTO the rfbEventAttendanceDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new rfbEventAttendanceDTO, or with status 400 (Bad Request) if the rfbEventAttendance has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rfb-event-attendances")
    @Timed
    public ResponseEntity<RfbEventAttendanceDTO> createRfbEventAttendance(@RequestBody RfbEventAttendanceDTO rfbEventAttendanceDTO) throws URISyntaxException {
        log.debug("REST request to save RfbEventAttendance : {}", rfbEventAttendanceDTO);
        if (rfbEventAttendanceDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new rfbEventAttendance cannot already have an ID")).body(null);
        }
        RfbEventAttendanceDTO result = rfbEventAttendanceService.save(rfbEventAttendanceDTO);
        return ResponseEntity.created(new URI("/api/rfb-event-attendances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rfb-event-attendances : Updates an existing rfbEventAttendance.
     *
     * @param rfbEventAttendanceDTO the rfbEventAttendanceDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated rfbEventAttendanceDTO,
     * or with status 400 (Bad Request) if the rfbEventAttendanceDTO is not valid,
     * or with status 500 (Internal Server Error) if the rfbEventAttendanceDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/rfb-event-attendances")
    @Timed
    public ResponseEntity<RfbEventAttendanceDTO> updateRfbEventAttendance(@RequestBody RfbEventAttendanceDTO rfbEventAttendanceDTO) throws URISyntaxException {
        log.debug("REST request to update RfbEventAttendance : {}", rfbEventAttendanceDTO);
        if (rfbEventAttendanceDTO.getId() == null) {
            return createRfbEventAttendance(rfbEventAttendanceDTO);
        }
        RfbEventAttendanceDTO result = rfbEventAttendanceService.save(rfbEventAttendanceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, rfbEventAttendanceDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rfb-event-attendances : get all the rfbEventAttendances.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of rfbEventAttendances in body
     */
    @GetMapping("/rfb-event-attendances")
    @Timed
    public ResponseEntity<List<RfbEventAttendanceDTO>> getAllRfbEventAttendances(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of RfbEventAttendances");
        Page<RfbEventAttendanceDTO> page = rfbEventAttendanceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/rfb-event-attendances");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /rfb-event-attendances/:id : get the "id" rfbEventAttendance.
     *
     * @param id the id of the rfbEventAttendanceDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the rfbEventAttendanceDTO, or with status 404 (Not Found)
     */
    @GetMapping("/rfb-event-attendances/{id}")
    @Timed
    public ResponseEntity<RfbEventAttendanceDTO> getRfbEventAttendance(@PathVariable Long id) {
        log.debug("REST request to get RfbEventAttendance : {}", id);
        RfbEventAttendanceDTO rfbEventAttendanceDTO = rfbEventAttendanceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rfbEventAttendanceDTO));
    }

    /**
     * DELETE  /rfb-event-attendances/:id : delete the "id" rfbEventAttendance.
     *
     * @param id the id of the rfbEventAttendanceDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/rfb-event-attendances/{id}")
    @Timed
    public ResponseEntity<Void> deleteRfbEventAttendance(@PathVariable Long id) {
        log.debug("REST request to delete RfbEventAttendance : {}", id);
        rfbEventAttendanceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
