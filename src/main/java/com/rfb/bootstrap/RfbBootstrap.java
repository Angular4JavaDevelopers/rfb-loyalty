package com.rfb.bootstrap;

import com.rfb.repository.RfbEventAttendanceRepository;
import com.rfb.repository.RfbEventRepository;
import com.rfb.repository.RfbLocationRepository;
import com.rfb.repository.RfbUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by jt on 10/14/17.
 */
@Component
public class RfbBootstrap implements CommandLineRunner {

    private final RfbLocationRepository rfbLocationRepository;
    private final RfbEventRepository rfbEventRepository;
    private final RfbEventAttendanceRepository rfbEventAttendanceRepository;
    private final RfbUserRepository rfbUserRepository;

    public RfbBootstrap(RfbLocationRepository rfbLocationRepository, RfbEventRepository rfbEventRepository,
                        RfbEventAttendanceRepository rfbEventAttendanceRepository, RfbUserRepository rfbUserRepository) {
        this.rfbLocationRepository = rfbLocationRepository;
        this.rfbEventRepository = rfbEventRepository;
        this.rfbEventAttendanceRepository = rfbEventAttendanceRepository;
        this.rfbUserRepository = rfbUserRepository;
    }

    @Override
    public void run(String... strings) throws Exception {

    }
}
