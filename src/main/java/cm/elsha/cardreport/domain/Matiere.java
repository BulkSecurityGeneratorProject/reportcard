package cm.elsha.cardreport.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Matiere.
 */
@Entity
@Table(name = "matiere")
public class Matiere implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "libelle", nullable = false)
    private String libelle;

    @ManyToOne
    private Categorie categorie;

    @OneToMany(mappedBy = "matiere")
    @JsonIgnore
    private Set<EstDispense> dispenses = new HashSet<>();

    @OneToMany(mappedBy = "matiere")
    @JsonIgnore
    private Set<Evaluation> evaluations = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public Set<EstDispense> getDispenses() {
        return dispenses;
    }

    public void setDispenses(Set<EstDispense> estDispenses) {
        this.dispenses = estDispenses;
    }

    public Set<Evaluation> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(Set<Evaluation> evaluations) {
        this.evaluations = evaluations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Matiere matiere = (Matiere) o;
        if(matiere.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, matiere.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Matiere{" +
            "id=" + id +
            ", libelle='" + libelle + "'" +
            '}';
    }
}
