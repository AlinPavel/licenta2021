package com.licenta.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A Review.
 */
@Entity
@Table(name = "review")
public class Review implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReview;

    @Column(name = "nota")
    private Integer nota;

    @Column(name = "comentariu")
    private String comentariu;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "idCatalogs", "idSports", "idReviews" }, allowSetters = true)
    private AppUser appUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getIdReview() {
        return idReview;
    }

    public void setIdReview(Long idReview) {
        this.idReview = idReview;
    }

    public Review idReview(Long idReview) {
        this.idReview = idReview;
        return this;
    }

    public Integer getNota() {
        return this.nota;
    }

    public Review nota(Integer nota) {
        this.nota = nota;
        return this;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }

    public String getComentariu() {
        return this.comentariu;
    }

    public Review comentariu(String comentariu) {
        this.comentariu = comentariu;
        return this;
    }

    public void setComentariu(String comentariu) {
        this.comentariu = comentariu;
    }

    public AppUser getAppUser() {
        return this.appUser;
    }

    public Review appUser(AppUser appUser) {
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
        if (!(o instanceof Review)) {
            return false;
        }
        return idReview != null && idReview.equals(((Review) o).idReview);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Review{" +
            "idReview=" + getIdReview() +
            ", nota=" + getNota() +
            ", comentariu='" + getComentariu() + "'" +
            "}";
    }
}
