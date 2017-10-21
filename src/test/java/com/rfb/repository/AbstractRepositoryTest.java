package com.rfb.repository;

import org.springframework.beans.factory.annotation.Autowired;

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
    RfbUserRepository rfbUserRepository;
}
