package ma.fstt.ecommerce.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "commande")
@NamedQueries({
    @NamedQuery(name = "Commande.findAll", query = "SELECT c FROM Commande c ORDER BY c.dateCommande DESC"),
    @NamedQuery(name = "Commande.findByInternaute", query = "SELECT c FROM Commande c WHERE c.internaute.id = :internauteId ORDER BY c.dateCommande DESC")
})
public class Commande implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_commande", unique = true, nullable = false, length = 50)
    private String numeroCommande;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_commande", nullable = false)
    private Date dateCommande;

    @Column(name = "montant_total", nullable = false)
    private Double montantTotal;

    @Column(name = "frais_livraison")
    private Double fraisLivraison = 0.0;

    @Column(nullable = false, length = 30)
    private String statut = "VALIDEE"; // EN_ATTENTE, VALIDEE, EXPEDIEE, LIVREE, ANNULEE

    @Column(name = "adresse_livraison", length = 400)
    private String adresseLivraison;

    @Column(name = "ville_livraison", length = 100)
    private String villeLivraison;

    @Column(name = "telephone_contact", length = 30)
    private String telephoneContact;

    @Column(name = "mode_paiement", length = 50)
    private String modePaiement = "PAIEMENT_LIVRAISON";

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "internaute_id", nullable = false)
    private Internaute internaute;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<LigneCommande> lignesCommande = new ArrayList<>();

    public Commande() {
        this.dateCommande = new Date();
    }

    public int getNombreArticles() {
        if (lignesCommande == null) return 0;
        int total = 0;
        for (LigneCommande lc : lignesCommande) {
            if (lc.getQuantite() != null) {
                total += lc.getQuantite();
            }
        }
        return total;
    }

    public String getModePaiementLibelle() {
        if ("CARTE_BANCAIRE".equals(modePaiement)) return "Carte Bancaire (CMI / Visa)";
        if ("VIREMENT".equals(modePaiement)) return "Virement Bancaire";
        return "Paiement à la livraison (Cash)";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroCommande() {
        return numeroCommande;
    }

    public void setNumeroCommande(String numeroCommande) {
        this.numeroCommande = numeroCommande;
    }

    public Date getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(Date dateCommande) {
        this.dateCommande = dateCommande;
    }

    public Double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(Double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public Double getFraisLivraison() {
        return fraisLivraison;
    }

    public void setFraisLivraison(Double fraisLivraison) {
        this.fraisLivraison = fraisLivraison;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getAdresseLivraison() {
        return adresseLivraison;
    }

    public void setAdresseLivraison(String adresseLivraison) {
        this.adresseLivraison = adresseLivraison;
    }

    public String getVilleLivraison() {
        return villeLivraison;
    }

    public void setVilleLivraison(String villeLivraison) {
        this.villeLivraison = villeLivraison;
    }

    public String getTelephoneContact() {
        return telephoneContact;
    }

    public void setTelephoneContact(String telephoneContact) {
        this.telephoneContact = telephoneContact;
    }

    public String getModePaiement() {
        return modePaiement;
    }

    public void setModePaiement(String modePaiement) {
        this.modePaiement = modePaiement;
    }

    public Internaute getInternaute() {
        return internaute;
    }

    public void setInternaute(Internaute internaute) {
        this.internaute = internaute;
    }

    public List<LigneCommande> getLignesCommande() {
        return lignesCommande;
    }

    public void setLignesCommande(List<LigneCommande> lignesCommande) {
        this.lignesCommande = lignesCommande;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Commande)) return false;
        Commande commande = (Commande) o;
        return id != null && Objects.equals(id, commande.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
