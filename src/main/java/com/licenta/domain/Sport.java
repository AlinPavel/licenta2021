package com.licenta.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A Sport.
 */
@Entity
@Table(name = "sport")
public class Sport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSport;

    @Column(name = "nume")
    private String nume;

    @Column(name = "locatie")
    private String locatie;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "idCatalogs", "idSports", "idReviews" }, allowSetters = true)
    private AppUser appUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getIdSport() {
        return idSport;
    }

    public void setIdSport(Long idSport) {
        this.idSport = idSport;
    }

    public Sport idSport(Long idSport) {
        this.idSport = idSport;
        return this;
    }

    public String getNume() {
        return this.nume;
    }

    public Sport nume(String nume) {
        this.nume = nume;
        return this;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getLocatie() {
        return this.locatie;
    }

    public Sport locatie(String locatie) {
        this.locatie = locatie;
        return this;
    }

    public void setLocatie(String locatie) {
        this.locatie = locatie;
    }

    public AppUser getAppUser() {
        return this.appUser;
    }

    public Sport appUser(AppUser appUser) {
        this.setAppUser(appUser);
        return this;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sport)) {
            return false;
        }
        return idSport != null && idSport.equals(((Sport) o).idSport);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Sport{" +
            "idSport=" + getIdSport() +
            ", nume='" + getNume() + "'" +
            ", locatie='" + getLocatie() + "'" +
            "}";
    }
}
