package com.licenta.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.licenta.domain.Sport} entity.
 */
public class SportDTO implements Serializable {

    private Long idSport;

    private String nume;

    private String locatie;

    private AppUserDTO appUser;

    public Long getIdSport() {
        return idSport;
    }

    public void setIdSport(Long idSport) {
        this.idSport = idSport;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getLocatie() {
        return locatie;
    }

    public void setLocatie(String locatie) {
        this.locatie = locatie;
    }

    public AppUserDTO getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUserDTO appUser) {
        this.appUser = appUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SportDTO)) {
            return false;
        }

        SportDTO sportDTO = (SportDTO) o;
        if (this.idSport == null) {
            return false;
        }
        return Objects.equals(this.idSport, sportDTO.idSport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.idSport);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SportDTO{" +
            "idSport=" + getIdSport() +
            ", nume='" + getNume() + "'" +
            ", locatie='" + getLocatie() + "'" +
            ", appUser=" + getAppUser() +
            "}";
    }
}
