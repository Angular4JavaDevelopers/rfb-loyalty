package com.rfb.service.dto;


import com.rfb.domain.RfbLocation;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the RfbUser entity.
 */
public class RfbUserDTO implements Serializable {

    private Long id;

    private String username;

    private RfbLocationDTO rfbLocationDTO;

    private Long homeLocationId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public RfbLocationDTO getRfbLocationDTO() {
        return rfbLocationDTO;
    }

    public void setRfbLocationDTO(RfbLocationDTO rfbLocationDTO) {
        this.rfbLocationDTO = rfbLocationDTO;
    }

    public Long getHomeLocationId() {
        return homeLocationId;
    }

    public void setHomeLocationId(Long homeLocationId) {
        this.homeLocationId = homeLocationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RfbUserDTO rfbUserDTO = (RfbUserDTO) o;
        if(rfbUserDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rfbUserDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RfbUserDTO{" +
            "id=" + getId() +
            ", username='" + getUsername() + "'" +
            "}";
    }
}
