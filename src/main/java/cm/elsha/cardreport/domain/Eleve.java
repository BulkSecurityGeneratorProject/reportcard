package cm.elsha.cardreport.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import cm.elsha.cardreport.domain.enumeration.Sexe;

/**
 * A Eleve.
 */
@Entity
@Table(name = "eleve")
public class Eleve implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "matricule")
    private String matricule;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @NotNull
    @Column(name = "datenaissance", nullable = false)
    private LocalDate datenaissance;

    @Column(name = "lieunaissance")
    private String lieunaissance;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexe")
    private Sexe sexe;

    @OneToMany(mappedBy = "eleve")
    @JsonIgnore
    private Set<Evaluation> passes = new HashSet<>();

    @OneToMany(mappedBy = "eleve")
    @JsonIgnore
    private Set<Inscrire> inscrires = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public LocalDate getDatenaissance() {
        return datenaissance;
    }

    public void setDatenaissance(LocalDate datenaissance) {
        this.datenaissance = datenaissance;
    }

    public String getLieunaissance() {
        return lieunaissance;
    }

    public void setLieunaissance(String lieunaissance) {
        this.lieunaissance = lieunaissance;
    }

    public Sexe getSexe() {
        return sexe;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public Set<Evaluation> getPasses() {
        return passes;
    }

    public void setPasses(Set<Evaluation> evaluations) {
        this.passes = evaluations;
    }

    public Set<Inscrire> getInscrires() {
        return inscrires;
    }

    public void setInscrires(Set<Inscrire> inscrires) {
        this.inscrires = inscrires;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Eleve eleve = (Eleve) o;
        if(eleve.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, eleve.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Eleve{" +
            "id=" + id +
            ", matricule='" + matricule + "'" +
            ", nom='" + nom + "'" +
            ", prenom='" + prenom + "'" +
            ", datenaissance='" + datenaissance + "'" +
            ", lieunaissance='" + lieunaissance + "'" +
            ", sexe='" + sexe + "'" +
            '}';
    }
}
