package cm.elsha.cardreport.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A AnneeAcademique.
 */
@Entity
@Table(name = "annee_academique")
public class AnneeAcademique implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Min(value = 2016)
    @Max(value = 2050)
    @Column(name = "anneedebut", nullable = false)
    private Integer anneedebut;

    @NotNull
    @Min(value = 2017)
    @Max(value = 2051)
    @Column(name = "anneefin", nullable = false)
    private Integer anneefin;

    @OneToMany(mappedBy = "anneeAcademique")
    @JsonIgnore
    private Set<Evaluation> evaluers = new HashSet<>();

    @OneToMany(mappedBy = "anneeAcademique")
    @JsonIgnore
    private Set<Inscrire> inscriptions = new HashSet<>();

    @OneToMany(mappedBy = "anneeAcademique")
    @JsonIgnore
    private Set<EstDispense> dispensers = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAnneedebut() {
        return anneedebut;
    }

    public void setAnneedebut(Integer anneedebut) {
        this.anneedebut = anneedebut;
    }

    public Integer getAnneefin() {
        return anneefin;
    }

    public void setAnneefin(Integer anneefin) {
        this.anneefin = anneefin;
    }

    public Set<Evaluation> getEvaluers() {
        return evaluers;
    }

    public void setEvaluers(Set<Evaluation> evaluations) {
        this.evaluers = evaluations;
    }

    public Set<Inscrire> getInscriptions() {
        return inscriptions;
    }

    public void setInscriptions(Set<Inscrire> inscrires) {
        this.inscriptions = inscrires;
    }

    public Set<EstDispense> getDispensers() {
        return dispensers;
    }

    public void setDispensers(Set<EstDispense> estDispenses) {
        this.dispensers = estDispenses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AnneeAcademique anneeAcademique = (AnneeAcademique) o;
        if(anneeAcademique.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, anneeAcademique.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AnneeAcademique{" +
            "id=" + id +
            ", anneedebut='" + anneedebut + "'" +
            ", anneefin='" + anneefin + "'" +
            '}';
    }
}
