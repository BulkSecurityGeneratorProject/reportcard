package cm.elsha.cardreport.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Inscrire.
 */
@Entity
@Table(name = "inscrire")
public class Inscrire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Eleve eleve;

    @ManyToOne
    private Classe classe;

    @ManyToOne
    private AnneeAcademique anneeAcademique;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Eleve getEleve() {
        return eleve;
    }

    public void setEleve(Eleve eleve) {
        this.eleve = eleve;
    }

    public Classe getClasse() {
        return classe;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
    }

    public AnneeAcademique getAnneeAcademique() {
        return anneeAcademique;
    }

    public void setAnneeAcademique(AnneeAcademique anneeAcademique) {
        this.anneeAcademique = anneeAcademique;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Inscrire inscrire = (Inscrire) o;
        if(inscrire.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, inscrire.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Inscrire{" +
            "id=" + id +
            '}';
    }
}
