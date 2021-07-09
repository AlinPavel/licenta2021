package com.licenta.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A AppUser.
 */
@Entity
@Table(name = "app_user")
public class AppUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAppUser;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "c_np")
    private Long cNP;

    @Column(name = "grupa")
    private Integer grupa;

    @Column(name = "an")
    private Integer an;

    @Column(name = "disciplina")
    private String disciplina;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "appUser")
    @JsonIgnoreProperties(value = { "appUser" }, allowSetters = true)
    private Set<Catalog> idCatalogs = new HashSet<>();

    @OneToMany(mappedBy = "appUser")
    @JsonIgnoreProperties(value = { "appUser" }, allowSetters = true)
    private Set<Sport> idSports = new HashSet<>();

    @OneToMany(mappedBy = "appUser")
    @JsonIgnoreProperties(value = { "appUser" }, allowSetters = true)
    private Set<Review> idReviews = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getIdAppUser() {
        return idAppUser;
    }

    public void setIdAppUser(Long idAppUser) {
        this.idAppUser = idAppUser;
    }

    public AppUser idAppUser(Long idAppUser) {
        this.idAppUser = idAppUser;
        return this;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public AppUser firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public AppUser lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getcNP() {
        return this.cNP;
    }

    public AppUser cNP(Long cNP) {
        this.cNP = cNP;
        return this;
    }

    public void setcNP(Long cNP) {
        this.cNP = cNP;
    }

    public Integer getGrupa() {
        return this.grupa;
    }

    public AppUser grupa(Integer grupa) {
        this.grupa = grupa;
        return this;
    }

    public void setGrupa(Integer grupa) {
        this.grupa = grupa;
    }

    public Integer getAn() {
        return this.an;
    }

    public AppUser an(Integer an) {
        this.an = an;
        return this;
    }

    public void setAn(Integer an) {
        this.an = an;
    }

    public String getDisciplina() {
        return this.disciplina;
    }

    public AppUser disciplina(String disciplina) {
        this.disciplina = disciplina;
        return this;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    public User getUser() {
        return this.user;
    }

    public AppUser user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Catalog> getIdCatalogs() {
        return this.idCatalogs;
    }

    public AppUser idCatalogs(Set<Catalog> catalogs) {
        this.setIdCatalogs(catalogs);
        return this;
    }

    public AppUser addIdCatalog(Catalog catalog) {
        this.idCatalogs.add(catalog);
        catalog.setAppUser(this);
        return this;
    }

    public AppUser removeIdCatalog(Catalog catalog) {
        this.idCatalogs.remove(catalog);
        catalog.setAppUser(null);
        return this;
    }

    public void setIdCatalogs(Set<Catalog> catalogs) {
        if (this.idCatalogs != null) {
            this.idCatalogs.forEach(i -> i.setAppUser(null));
        }
        if (catalogs != null) {
            catalogs.forEach(i -> i.setAppUser(this));
        }
        this.idCatalogs = catalogs;
    }

    public Set<Sport> getIdSports() {
        return this.idSports;
    }

    public AppUser idSports(Set<Sport> sports) {
        this.setIdSports(sports);
        return this;
    }

    public AppUser addIdSport(Sport sport) {
        this.idSports.add(sport);
        sport.setAppUser(this);
        return this;
    }

    public AppUser removeIdSport(Sport sport) {
        this.idSports.remove(sport);
        sport.setAppUser(null);
        return this;
    }

    public void setIdSports(Set<Sport> sports) {
        if (this.idSports != null) {
            this.idSports.forEach(i -> i.setAppUser(null));
        }
        if (sports != null) {
            sports.forEach(i -> i.setAppUser(this));
        }
        this.idSports = sports;
    }

    public Set<Review> getIdReviews() {
        return this.idReviews;
    }

    public AppUser idReviews(Set<Review> reviews) {
        this.setIdReviews(reviews);
        return this;
    }

    public AppUser addIdReview(Review review) {
        this.idReviews.add(review);
        review.setAppUser(this);
        return this;
    }

    public AppUser removeIdReview(Review review) {
        this.idReviews.remove(review);
        review.setAppUser(null);
        return this;
    }

    public void setIdReviews(Set<Review> reviews) {
        if (this.idReviews != null) {
            this.idReviews.forEach(i -> i.setAppUser(null));
        }
        if (reviews != null) {
            reviews.forEach(i -> i.setAppUser(this));
        }
        this.idReviews = reviews;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppUser)) {
            return false;
        }
        return idAppUser != null && idAppUser.equals(((AppUser) o).idAppUser);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppUser{" +
            "idAppUser=" + getIdAppUser() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", cNP=" + getcNP() +
            ", grupa=" + getGrupa() +
            ", an=" + getAn() +
            ", disciplina='" + getDisciplina() + "'" +
            "}";
    }
}
