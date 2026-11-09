package ma.fstt.ecommerce.beans;

import ma.fstt.ecommerce.dao.CategorieDAO;
import ma.fstt.ecommerce.dao.ProduitDAO;
import ma.fstt.ecommerce.entities.Categorie;
import ma.fstt.ecommerce.entities.Produit;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@ManagedBean(name = "vitrineBean")
@SessionScoped
public class VitrineBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private final ProduitDAO produitDAO = new ProduitDAO();
    private final CategorieDAO categorieDAO = new CategorieDAO();

    private List<Categorie> categories;
    private List<Produit> produits;

    private Long categorieSelectionneeId = 0L;
    private String motCleRecherche = "";
    private String triSelectionne = "VEDETTE";

    private Produit produitSelectionne;
    private Long detailProduitId;
    private int quantiteDetail = 1;

    @PostConstruct
    public void init() {
        rafraichirCatalogue();
    }

    public void rafraichirCatalogue() {
        this.categories = categorieDAO.listerTout();
        this.produits = produitDAO.rechercherEtFiltrer(categorieSelectionneeId, motCleRecherche, triSelectionne);
        if (this.produitSelectionne != null && this.produitSelectionne.getId() != null) {
            this.produitSelectionne = produitDAO.trouverParId(this.produitSelectionne.getId());
        }
    }

    public String filtrerParCategorie(Long catId) {
        this.categorieSelectionneeId = (catId != null) ? catId : 0L;
        rafraichirCatalogue();
        return "/index.xhtml?faces-redirect=true";
    }

    public String rechercher() {
        rafraichirCatalogue();
        return "/index.xhtml?faces-redirect=true";
    }

    public String appliquerTri() {
        rafraichirCatalogue();
        return null;
    }

    public String reinitialiserFiltres() {
        this.categorieSelectionneeId = 0L;
        this.motCleRecherche = "";
        this.triSelectionne = "VEDETTE";
        rafraichirCatalogue();
        return "/index.xhtml?faces-redirect=true";
    }

    public String voirDetail(Long produitId) {
        this.produitSelectionne = produitDAO.trouverParId(produitId);
        this.detailProduitId = produitId;
        this.quantiteDetail = 1;
        return "/produit.xhtml?faces-redirect=true";
    }

    public void chargerProduitDepuisParam() {
        if (detailProduitId != null) {
            Produit p = produitDAO.trouverParId(detailProduitId);
            if (p != null) {
                this.produitSelectionne = p;
                this.quantiteDetail = 1;
            }
        }
    }

    public List<Produit> getProduitsSimilaires() {
        if (produitSelectionne == null || produitSelectionne.getCategorie() == null) {
            return Collections.emptyList();
        }
        return produitDAO.listerSimilaires(
                produitSelectionne.getCategorie().getId(),
                produitSelectionne.getId(),
                4
        );
    }

    public String getNomCategorieActive() {
        if (categorieSelectionneeId == null || categorieSelectionneeId == 0L || categories == null) {
            return "Tous les produits";
        }
        for (Categorie c : categories) {
            if (c.getId().equals(categorieSelectionneeId)) {
                return c.getNom();
            }
        }
        return "Catalogue";
    }

    public List<Categorie> getCategories() {
        if (categories == null) {
            rafraichirCatalogue();
        }
        return categories;
    }

    public List<Produit> getProduits() {
        if (produits == null) {
            rafraichirCatalogue();
        }
        return produits;
    }

    public Long getCategorieSelectionneeId() {
        return categorieSelectionneeId;
    }

    public void setCategorieSelectionneeId(Long categorieSelectionneeId) {
        this.categorieSelectionneeId = categorieSelectionneeId;
    }

    public String getMotCleRecherche() {
        return motCleRecherche;
    }

    public void setMotCleRecherche(String motCleRecherche) {
        this.motCleRecherche = motCleRecherche;
    }

    public String getTriSelectionne() {
        return triSelectionne;
    }

    public void setTriSelectionne(String triSelectionne) {
        this.triSelectionne = triSelectionne;
    }

    public Produit getProduitSelectionne() {
        return produitSelectionne;
    }

    public void setProduitSelectionne(Produit produitSelectionne) {
        this.produitSelectionne = produitSelectionne;
    }

    public Long getDetailProduitId() {
        return detailProduitId;
    }

    public void setDetailProduitId(Long detailProduitId) {
        this.detailProduitId = detailProduitId;
    }

    public int getQuantiteDetail() {
        return quantiteDetail;
    }

    public void setQuantiteDetail(int quantiteDetail) {
        this.quantiteDetail = quantiteDetail;
    }
}
