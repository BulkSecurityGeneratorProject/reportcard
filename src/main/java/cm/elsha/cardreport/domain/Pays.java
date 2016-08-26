package cm.elsha.cardreport.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Pays.
 */
@Entity
@Table(name = "pays")
public class Pays implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "nompaysfr", nullable = false)
    private String nompaysfr;

    @NotNull
    @Column(name = "nompaysan", nullable = false)
    private String nompaysan;

    @NotNull
    @Column(name = "ministerefr", nullable = false)
    private String ministerefr;

    @NotNull
    @Column(name = "ministerean", nullable = false)
    private String ministerean;

    @NotNull
    @Column(name = "devisefr", nullable = false)
    private String devisefr;

    @NotNull
    @Column(name = "devisean", nullable = false)
    private String devisean;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNompaysfr() {
        return nompaysfr;
    }

    public void setNompaysfr(String nompaysfr) {
        this.nompaysfr = nompaysfr;
    }

    public String getNompaysan() {
        return nompaysan;
    }

    public void setNompaysan(String nompaysan) {
        this.nompaysan = nompaysan;
    }

    public String getMinisterefr() {
        return ministerefr;
    }

    public void setMinisterefr(String ministerefr) {
        this.ministerefr = ministerefr;
    }

    public String getMinisterean() {
        return ministerean;
    }

    public void setMinisterean(String ministerean) {
        this.ministerean = ministerean;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Pays pays = (Pays) o;
        if(pays.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, pays.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Pays{" +
            "id=" + id +
            ", nompaysfr='" + nompaysfr + "'" +
            ", nompaysan='" + nompaysan + "'" +
            ", ministerefr='" + ministerefr + "'" +
            ", ministerean='" + ministerean + "'" +
            ", devisefr='" + devisefr + "'" +
            ", devisean='" + devisean + "'" +
            '}';
    }
}
