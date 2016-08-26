package cm.elsha.cardreport.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Sequence.
 */
@Entity
@Table(name = "sequence")
public class Sequence implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private Integer nom;

    @OneToMany(mappedBy = "sequence")
    @JsonIgnore
    private Set<Evaluation> evaluations = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNom() {
        return nom;
    }

    public void setNom(Integer nom) {
        this.nom = nom;
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
        Sequence sequence = (Sequence) o;
        if(sequence.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, sequence.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Sequence{" +
            "id=" + id +
            ", nom='" + nom + "'" +
            '}';
    }
}
