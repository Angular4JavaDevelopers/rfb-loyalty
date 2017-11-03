package com.rfb.repository;

import com.rfb.RfbloyaltyApp;
import com.rfb.bootstrap.RfbBootstrap;
import com.rfb.domain.RfbLocation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.DayOfWeek;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RfbloyaltyApp.class})
public class RfbLocationRepositoryTest extends AbstractRepositoryTest{

    @Before
    public void setUp() throws Exception {
        RfbBootstrap rfbBootstrap = new RfbBootstrap(rfbLocationRepository, rfbEventRepository,
            rfbEventAttendanceRepository, userRepository, passwordEncoder, authorityRepository);
    }

    @Test
    public void findAllByRunDayOfWeek() throws Exception {
        List<RfbLocation> mondayLocations = rfbLocationRepository.findAllByRunDayOfWeek(DayOfWeek.MONDAY.getValue());
        List<RfbLocation> tuesDayLocations = rfbLocationRepository.findAllByRunDayOfWeek(DayOfWeek.TUESDAY.getValue());
        List<RfbLocation> wedLocations = rfbLocationRepository.findAllByRunDayOfWeek(DayOfWeek.WEDNESDAY.getValue());

        assertEquals(2, mondayLocations.size());
        assertEquals(2, tuesDayLocations.size());
        assertEquals(1, wedLocations.size());
    }

}
