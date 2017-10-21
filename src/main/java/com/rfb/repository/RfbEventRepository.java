package com.rfb.repository;

import com.rfb.domain.RfbEvent;
import com.rfb.domain.RfbLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;


/**
 * Spring Data JPA repository for the RfbEvent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RfbEventRepository extends JpaRepository<RfbEvent, Long> {

    RfbEvent findByRfbLocationAndEventDate(RfbLocation location, LocalDate date);

}
