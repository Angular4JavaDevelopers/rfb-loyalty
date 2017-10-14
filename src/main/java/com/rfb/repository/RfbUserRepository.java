package com.rfb.repository;

import com.rfb.domain.RfbUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the RfbUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RfbUserRepository extends JpaRepository<RfbUser, Long> {

}
