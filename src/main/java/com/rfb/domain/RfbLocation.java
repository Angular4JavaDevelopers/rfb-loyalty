package com.rfb.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A RfbLocation.
 */
@Entity
@Table(name = "rfb_location")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RfbLocation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "location_name")
    private String locationName;

    @Column(name = "run_day_of_week")
    private Integer runDayOfWeek;

    @OneToMany(mappedBy = "rfbLocation")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RfbEvent> rvbEvents = new HashSet<>();

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocationName() {
        return locationName;
    }

    public RfbLocation locationName(String locationName) {
        this.locationName = locationName;
        return this;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Integer getRunDayOfWeek() {
        return runDayOfWeek;
    }

    public RfbLocation runDayOfWeek(Integer runDayOfWeek) {
        this.runDayOfWeek = runDayOfWeek;
        return this;
    }

    public void setRunDayOfWeek(Integer runDayOfWeek) {
        this.runDayOfWeek = runDayOfWeek;
    }

    public Set<RfbEvent> getRvbEvents() {
        return rvbEvents;
    }

    public RfbLocation rvbEvents(Set<RfbEvent> rfbEvents) {
        this.rvbEvents = rfbEvents;
        return this;
    }

    public RfbLocation addRvbEvent(RfbEvent rfbEvent) {
        this.rvbEvents.add(rfbEvent);
        rfbEvent.setRfbLocation(this);
        return this;
    }

    public RfbLocation removeRvbEvent(RfbEvent rfbEvent) {
        this.rvbEvents.remove(rfbEvent);
        rfbEvent.setRfbLocation(null);
        return this;
    }

    public void setRvbEvents(Set<RfbEvent> rfbEvents) {
        this.rvbEvents = rfbEvents;
    }
    // jhipster-needle-entity-add-getters-setters - Jhipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RfbLocation rfbLocation = (RfbLocation) o;
        if (rfbLocation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rfbLocation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RfbLocation{" +
            "id=" + getId() +
            ", locationName='" + getLocationName() + "'" +
            ", runDayOfWeek='" + getRunDayOfWeek() + "'" +
            "}";
    }
}
