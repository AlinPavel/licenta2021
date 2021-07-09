package com.licenta.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.licenta.domain.AppUser} entity.
 */
public class AppUserDTO implements Serializable {

    private Long idAppUser;

    private String firstName;

    private String lastName;

    private Long cNP;

    private Integer grupa;

    private Integer an;

    private String disciplina;

    private UserDTO user;

    public Long getIdAppUser() {
        return idAppUser;
    }

    public void setIdAppUser(Long idAppUser) {
        this.idAppUser = idAppUser;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getcNP() {
        return cNP;
    }

    public void setcNP(Long cNP) {
        this.cNP = cNP;
    }

    public Integer getGrupa() {
        return grupa;
    }

    public void setGrupa(Integer grupa) {
        this.grupa = grupa;
    }

    public Integer getAn() {
        return an;
    }

    public void setAn(Integer an) {
        this.an = an;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppUserDTO)) {
            return false;
        }

        AppUserDTO appUserDTO = (AppUserDTO) o;
        if (this.idAppUser == null) {
            return false;
        }
        return Objects.equals(this.idAppUser, appUserDTO.idAppUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.idAppUser);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppUserDTO{" +
            "idAppUser=" + getIdAppUser() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", cNP=" + getcNP() +
            ", grupa=" + getGrupa() +
            ", an=" + getAn() +
            ", disciplina='" + getDisciplina() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
