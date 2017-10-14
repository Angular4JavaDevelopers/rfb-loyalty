package com.rfb.service.mapper;

import com.rfb.domain.RfbLocation;
import com.rfb.service.dto.RfbLocationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity RfbLocation and its DTO RfbLocationDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RfbLocationMapper extends EntityMapper <RfbLocationDTO, RfbLocation> {

    @Mapping(target = "rvbEvents", ignore = true)
    RfbLocation toEntity(RfbLocationDTO rfbLocationDTO);
    default RfbLocation fromId(Long id) {
        if (id == null) {
            return null;
        }
        RfbLocation rfbLocation = new RfbLocation();
        rfbLocation.setId(id);
        return rfbLocation;
    }
}
