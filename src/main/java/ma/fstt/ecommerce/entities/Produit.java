package ma.fstt.ecommerce.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "produit")
@NamedQueries({
    @NamedQuery(name = "Produit.findAll", query = "SELECT p FROM Produit p ORDER BY p.enVedette DESC, p.id DESC"),
    @NamedQuery(name = "Produit.findByCategorie", query = "SELECT p FROM Produit p WHERE p.categorie.id = :catId ORDER BY p.enVedette DESC, p.id DESC"),
    @NamedQuery(name = "Produit.search", query = "SELECT p FROM Produit p WHERE LOWER(p.libelle) LIKE :kw OR LOWER(p.marque) LIKE :kw OR LOWER(p.description) LIKE :kw ORDER BY p.enVedette DESC, p.id DESC")
})
public class Produit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 50)
    private String reference;

    @Column(nullable = false, length = 180)
    private String libelle;

    @Column(length = 100)
    private String marque;

    @Column(length = 1500)
    private String description;

    @Column(nullable = false)
    private Double prix;

    @Column(name = "ancien_prix")
    private Double ancienPrix;

    @Column(name = "quantite_stock", nullable = false)
    private Integer quantiteStock = 0;

    @Column(name = "image_url", length = 4000)
    private String imageUrl;

    @Column(name = "en_vedette")
    private Boolean enVedette = false;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_ajout")
    private Date dateAjout;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categorie_id")
    private Categorie categorie;

    public Produit() {
        this.dateAjout = new Date();
    }

    public Produit(String reference, String libelle, String marque, String description,
                   Double prix, Double ancienPrix, Integer quantiteStock,
                   String imageUrl, Boolean enVedette, Categorie categorie) {
        this.reference = reference;
        this.libelle = libelle;
        this.marque = marque;
        this.description = description;
        this.prix = prix;
        this.ancienPrix = ancienPrix;
        this.quantiteStock = quantiteStock;
        this.imageUrl = imageUrl;
        this.enVedette = enVedette;
        this.categorie = categorie;
        this.dateAjout = new Date();
    }

    public boolean isEnStock() {
        return quantiteStock != null && quantiteStock > 0;
    }

    public boolean isEnPromotion() {
        return ancienPrix != null && prix != null && ancienPrix > prix;
    }

    public int getRemisePourcentage() {
        if (!isEnPromotion()) {
            return 0;
        }
        return (int) Math.round(((ancienPrix - prix) / ancienPrix) * 100.0);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public Double getAncienPrix() {
        return ancienPrix;
    }

    public void setAncienPrix(Double ancienPrix) {
        this.ancienPrix = ancienPrix;
    }

    public Integer getQuantiteStock() {
        return quantiteStock;
    }

    public void setQuantiteStock(Integer quantiteStock) {
        this.quantiteStock = quantiteStock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getEnVedette() {
        return enVedette;
    }

    public void setEnVedette(Boolean enVedette) {
        this.enVedette = enVedette;
    }

    public Date getDateAjout() {
        return dateAjout;
    }

    public void setDateAjout(Date dateAjout) {
        this.dateAjout = dateAjout;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Produit)) return false;
        Produit produit = (Produit) o;
        return id != null && Objects.equals(id, produit.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
