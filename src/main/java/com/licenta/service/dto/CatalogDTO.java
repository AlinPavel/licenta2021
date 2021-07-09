package com.licenta.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.licenta.domain.Catalog} entity.
 */
public class CatalogDTO implements Serializable {

    private Long idCatalog;

    private Double nota;

    private AppUserDTO appUser;

    public Long getIdCatalog() {
        return idCatalog;
    }

    public void setIdCatalog(Long idCatalog) {
        this.idCatalog = idCatalog;
    }

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
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
        if (!(o instanceof CatalogDTO)) {
            return false;
        }

        CatalogDTO catalogDTO = (CatalogDTO) o;
        if (this.idCatalog == null) {
            return false;
        }
        return Objects.equals(this.idCatalog, catalogDTO.idCatalog);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.idCatalog);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CatalogDTO{" +
            "idCatalog=" + getIdCatalog() +
            ", nota=" + getNota() +
            ", appUser=" + getAppUser() +
            "}";
    }
}
