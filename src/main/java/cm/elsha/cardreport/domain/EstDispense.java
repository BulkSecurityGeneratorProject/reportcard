package cm.elsha.cardreport.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A EstDispense.
 */
@Entity
@Table(name = "est_dispense")
public class EstDispense implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Min(value = 2016)
    @Max(value = 2050)
    @Column(name = "annee")
    private Integer annee;

    @NotNull
    @Column(name = "coefficient", nullable = false)
    private Integer coefficient;

    @ManyToOne
    private Matiere matiere;

    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAnnee() {
        return annee;
    }

    public void setAnnee(Integer annee) {
        this.annee = annee;
    }

    public Integer getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(Integer coefficient) {
        this.coefficient = coefficient;
    }

    public Matiere getMatiere() {
        return matiere;
    }

    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EstDispense estDispense = (EstDispense) o;
        if(estDispense.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, estDispense.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EstDispense{" +
            "id=" + id +
            ", annee='" + annee + "'" +
            ", coefficient='" + coefficient + "'" +
            '}';
    }
}
