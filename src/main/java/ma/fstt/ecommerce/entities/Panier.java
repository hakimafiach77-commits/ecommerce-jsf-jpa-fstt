package ma.fstt.ecommerce.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "panier")
public class Panier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_creation")
    private Date dateCreation;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_modification")
    private Date dateModification;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "internaute_id", unique = true)
    private Internaute internaute;

    @OneToMany(mappedBy = "panier", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<LignePanier> lignes = new ArrayList<>();

    public Panier() {
        this.dateCreation = new Date();
        this.dateModification = new Date();
    }

    public Panier(Internaute internaute) {
        this();
        this.internaute = internaute;
    }

    public int getNombreArticles() {
        if (lignes == null) return 0;
        int total = 0;
        for (LignePanier lp : lignes) {
            if (lp.getQuantite() != null) {
                total += lp.getQuantite();
            }
        }
        return total;
    }

    public double getSousTotalTTC() {
        if (lignes == null) return 0.0;
        double sum = 0.0;
        for (LignePanier lp : lignes) {
            sum += lp.getSousTotal();
        }
        return Math.round(sum * 100.0) / 100.0;
    }

    public double getTotalHT() {
        return Math.round((getSousTotalTTC() / 1.20) * 100.0) / 100.0;
    }

    public double getMontantTVA() {
        return Math.round((getSousTotalTTC() - getTotalHT()) * 100.0) / 100.0;
    }

    public double getFraisLivraison() {
        double sousTotal = getSousTotalTTC();
        if (sousTotal <= 0.0) return 0.0;
        return sousTotal >= 1000.0 ? 0.0 : 49.0;
    }

    public double getTotalGeneral() {
        return Math.round((getSousTotalTTC() + getFraisLivraison()) * 100.0) / 100.0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Date getDateModification() {
        return dateModification;
    }

    public void setDateModification(Date dateModification) {
        this.dateModification = dateModification;
    }

    public Internaute getInternaute() {
        return internaute;
    }

    public void setInternaute(Internaute internaute) {
        this.internaute = internaute;
    }

    public List<LignePanier> getLignes() {
        return lignes;
    }

    public void setLignes(List<LignePanier> lignes) {
        this.lignes = lignes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Panier)) return false;
        Panier panier = (Panier) o;
        return id != null && Objects.equals(id, panier.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
