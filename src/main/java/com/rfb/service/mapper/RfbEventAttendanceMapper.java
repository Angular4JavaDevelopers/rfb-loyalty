package com.rfb.service.mapper;

import com.rfb.domain.RfbEventAttendance;
import com.rfb.service.dto.RfbEventAttendanceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity RfbEventAttendance and its DTO RfbEventAttendanceDTO.
 */
@Mapper(componentModel = "spring", uses = {RfbEventMapper.class, UserMapper.class, })
public interface RfbEventAttendanceMapper extends EntityMapper <RfbEventAttendanceDTO, RfbEventAttendance> {

    @Mapping(source = "rfbEvent", target = "rfbEventDTO")
    @Mapping(source = "user", target = "userDTO")
    RfbEventAttendanceDTO toDto(RfbEventAttendance rfbEventAttendance);

    @Mapping(source = "rfbEventDTO", target = "rfbEvent")
    @Mapping(source = "userDTO", target = "user")
    RfbEventAttendance toEntity(RfbEventAttendanceDTO rfbEventAttendanceDTO);
    default RfbEventAttendance fromId(Long id) {
        if (id == null) {
            return null;
        }
        RfbEventAttendance rfbEventAttendance = new RfbEventAttendance();
        rfbEventAttendance.setId(id);
        return rfbEventAttendance;
    }
}
