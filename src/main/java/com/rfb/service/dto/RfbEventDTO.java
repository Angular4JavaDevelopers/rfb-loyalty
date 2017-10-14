package com.rfb.service.dto;


import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the RfbEvent entity.
 */
public class RfbEventDTO implements Serializable {

    private Long id;

    private LocalDate eventDate;

    private String eventCode;

    private Long rfbLocationId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public Long getRfbLocationId() {
        return rfbLocationId;
    }

    public void setRfbLocationId(Long rfbLocationId) {
        this.rfbLocationId = rfbLocationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RfbEventDTO rfbEventDTO = (RfbEventDTO) o;
        if(rfbEventDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rfbEventDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RfbEventDTO{" +
            "id=" + getId() +
            ", eventDate='" + getEventDate() + "'" +
            ", eventCode='" + getEventCode() + "'" +
            "}";
    }
}
