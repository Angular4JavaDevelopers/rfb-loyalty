package com.rfb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A RfbUser.
 */
@Entity
@Table(name = "rfb_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RfbUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @OneToOne
    @JoinColumn(unique = true)
    private RfbLocation homeLocation;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public RfbUser username(String username) {
        this.username = username;
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public RfbLocation getHomeLocation() {
        return homeLocation;
    }

    public RfbUser homeLocation(RfbLocation rfbLocation) {
        this.homeLocation = rfbLocation;
        return this;
    }

    public void setHomeLocation(RfbLocation rfbLocation) {
        this.homeLocation = rfbLocation;
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
        RfbUser rfbUser = (RfbUser) o;
        if (rfbUser.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rfbUser.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RfbUser{" +
            "id=" + getId() +
            ", username='" + getUsername() + "'" +
            "}";
    }
}
