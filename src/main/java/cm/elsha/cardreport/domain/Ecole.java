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
    @Column(name = "noman", nullable = false)
    private String noman;

    @NotNull
    @Column(name = "devisefr", nullable = false)
    private String devisefr;

    @NotNull
    @Column(name = "devisean", nullable = false)
    private String devisean;

    @Column(name = "boitepostal")
    private String boitepostal;

    @Lob
    @Column(name = "logo")
    private byte[] logo;

    @Column(name = "logo_content_type")
    private String logoContentType;

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

    public String getNoman() {
        return noman;
    }

    public void setNoman(String noman) {
        this.noman = noman;
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

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getLogoContentType() {
        return logoContentType;
    }

    public void setLogoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
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
            ", noman='" + noman + "'" +
            ", devisefr='" + devisefr + "'" +
            ", devisean='" + devisean + "'" +
            ", boitepostal='" + boitepostal + "'" +
            ", logo='" + logo + "'" +
            ", logoContentType='" + logoContentType + "'" +
            '}';
    }
}
