package com.licenta.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.licenta.domain.Review} entity.
 */
public class ReviewDTO implements Serializable {

    private Long idReview;

    private Integer nota;

    private String comentariu;

    private AppUserDTO appUser;

    public Long getIdReview() {
        return idReview;
    }

    public void setIdReview(Long idReview) {
        this.idReview = idReview;
    }

    public Integer getNota() {
        return nota;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }

    public String getComentariu() {
        return comentariu;
    }

    public void setComentariu(String comentariu) {
        this.comentariu = comentariu;
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
        if (!(o instanceof ReviewDTO)) {
            return false;
        }

        ReviewDTO reviewDTO = (ReviewDTO) o;
        if (this.idReview == null) {
            return false;
        }
        return Objects.equals(this.idReview, reviewDTO.idReview);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.idReview);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReviewDTO{" +
            "idReview=" + getIdReview() +
            ", nota=" + getNota() +
            ", comentariu='" + getComentariu() + "'" +
            ", appUser=" + getAppUser() +
            "}";
    }
}
