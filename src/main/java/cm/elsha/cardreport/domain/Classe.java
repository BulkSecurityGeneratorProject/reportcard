package cm.elsha.cardreport.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Classe.
 */
@Entity
@Table(name = "classe")
public class Classe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotNull
    @Min(value = 1)
    @Max(value = 2)
    @Column(name = "cycle", nullable = false)
    private Integer cycle;

    @OneToMany(mappedBy = "classe")
    @JsonIgnore
    private Set<Inscrire> inscrires = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Integer getCycle() {
        return cycle;
    }

    public void setCycle(Integer cycle) {
        this.cycle = cycle;
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
        Classe classe = (Classe) o;
        if(classe.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, classe.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Classe{" +
            "id=" + id +
            ", nom='" + nom + "'" +
            ", cycle='" + cycle + "'" +
            '}';
    }
}
