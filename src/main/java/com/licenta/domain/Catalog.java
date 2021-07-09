package com.licenta.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A Catalog.
 */
@Entity
@Table(name = "catalog")
public class Catalog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCatalog;

    @Column(name = "nota")
    private Double nota;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "idCatalogs", "idSports", "idReviews" }, allowSetters = true)
    private AppUser appUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getIdCatalog() {
        return idCatalog;
    }

    public void setIdCatalog(Long idCatalog) {
        this.idCatalog = idCatalog;
    }

    public Catalog idCatalog(Long idCatalog) {
        this.idCatalog = idCatalog;
        return this;
    }

    public Double getNota() {
        return this.nota;
    }

    public Catalog nota(Double nota) {
        this.nota = nota;
        return this;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }

    public AppUser getAppUser() {
        return this.appUser;
    }

    public Catalog appUser(AppUser appUser) {
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
        if (!(o instanceof Catalog)) {
            return false;
        }
        return idCatalog != null && idCatalog.equals(((Catalog) o).idCatalog);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Catalog{" +
            "idCatalog=" + getIdCatalog() +
            ", nota=" + getNota() +
            "}";
    }
}
