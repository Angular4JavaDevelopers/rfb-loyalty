package com.rfb.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by jt on 10/21/17.
 */
abstract class AbstractRepositoryTest {
    @Autowired
    RfbLocationRepository rfbLocationRepository;

    @Autowired
    RfbEventRepository rfbEventRepository;

    @Autowired
    RfbEventAttendanceRepository rfbEventAttendanceRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthorityRepository authorityRepository;

}
