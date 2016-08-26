package cm.elsha.cardreport.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Ecole.
 */
@Entity
@Table(name = "ecole")
public class Ecole implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "nomfr", nullable = false)
    private String nomfr;

    @NotNull
    @Column(name = "nomfr", nullable = false)
    private String nomfr;

    @NotNull
    @Column(name = "devisefr", nullable = false)
    private String devisefr;

    @NotNull
    @Column(name = "devisean", nullable = false)
    private String devisean;

    @Column(name = "boitepostal")
    private String boitepostal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomfr() {
        return nomfr;
    }

    public void setNomfr(String nomfr) {
        this.nomfr = nomfr;
    }

    public String getNomfr() {
        return nomfr;
    }

    public void setNomfr(String nomfr) {
        this.nomfr = nomfr;
    }

    public String getDevisefr() {
        return devisefr;
    }

    public void setDevisefr(String devisefr) {
        this.devisefr = devisefr;
    }

    public String getDevisean() {
        return devisean;
    }

    public void setDevisean(String devisean) {
        this.devisean = devisean;
    }

    public String getBoitepostal() {
        return boitepostal;
    }

    public void setBoitepostal(String boitepostal) {
        this.boitepostal = boitepostal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ecole ecole = (Ecole) o;
        if(ecole.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, ecole.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Ecole{" +
            "id=" + id +
            ", nomfr='" + nomfr + "'" +
            ", nomfr='" + nomfr + "'" +
            ", devisefr='" + devisefr + "'" +
            ", devisean='" + devisean + "'" +
            ", boitepostal='" + boitepostal + "'" +
            '}';
    }
}
