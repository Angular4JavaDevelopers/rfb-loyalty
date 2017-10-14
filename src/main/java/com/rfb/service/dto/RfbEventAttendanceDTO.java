package com.rfb.service.dto;


import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the RfbEventAttendance entity.
 */
public class RfbEventAttendanceDTO implements Serializable {

    private Long id;

    private LocalDate attendanceDate;

    private Long rfbEventId;

    private Long rfbUserId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public Long getRfbEventId() {
        return rfbEventId;
    }

    public void setRfbEventId(Long rfbEventId) {
        this.rfbEventId = rfbEventId;
    }

    public Long getRfbUserId() {
        return rfbUserId;
    }

    public void setRfbUserId(Long rfbUserId) {
        this.rfbUserId = rfbUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RfbEventAttendanceDTO rfbEventAttendanceDTO = (RfbEventAttendanceDTO) o;
        if(rfbEventAttendanceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rfbEventAttendanceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RfbEventAttendanceDTO{" +
            "id=" + getId() +
            ", attendanceDate='" + getAttendanceDate() + "'" +
            "}";
    }
}
