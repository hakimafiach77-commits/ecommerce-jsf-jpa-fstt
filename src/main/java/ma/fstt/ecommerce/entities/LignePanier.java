package ma.fstt.ecommerce.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "ligne_panier")
public class LignePanier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer quantite = 1;

    @Column(name = "prix_unitaire", nullable = false)
    private Double prixUnitaire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "panier_id")
    private Panier panier;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "produit_id", nullable = false)
    private Produit produit;

    public LignePanier() {
    }

    public LignePanier(Panier panier, Produit produit, Integer quantite) {
        this.panier = panier;
        this.produit = produit;
        this.quantite = quantite;
        this.prixUnitaire = produit != null ? produit.getPrix() : 0.0;
    }

    public double getSousTotal() {
        if (quantite == null || prixUnitaire == null) return 0.0;
        return Math.round((quantite * prixUnitaire) * 100.0) / 100.0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public Double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(Double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public Panier getPanier() {
        return panier;
    }

    public void setPanier(Panier panier) {
        this.panier = panier;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LignePanier)) return false;
        LignePanier that = (LignePanier) o;
        if (id != null && that.id != null) {
            return Objects.equals(id, that.id);
        }
        return produit != null && that.produit != null && Objects.equals(produit.getId(), that.produit.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
