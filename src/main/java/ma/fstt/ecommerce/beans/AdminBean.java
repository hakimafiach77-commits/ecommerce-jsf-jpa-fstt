package ma.fstt.ecommerce.beans;

import ma.fstt.ecommerce.dao.CategorieDAO;
import ma.fstt.ecommerce.dao.CommandeDAO;
import ma.fstt.ecommerce.dao.InternauteDAO;
import ma.fstt.ecommerce.dao.ProduitDAO;
import ma.fstt.ecommerce.entities.Categorie;
import ma.fstt.ecommerce.entities.Commande;
import ma.fstt.ecommerce.entities.Internaute;
import ma.fstt.ecommerce.entities.Produit;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name = "adminBean")
@SessionScoped
public class AdminBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private final ProduitDAO produitDAO = new ProduitDAO();
    private final CategorieDAO categorieDAO = new CategorieDAO();
    private final CommandeDAO commandeDAO = new CommandeDAO();
    private final InternauteDAO internauteDAO = new InternauteDAO();

    private String ongletActif = "PRODUITS";

    // Formulaire Produit
    private Produit produitForm = new Produit();
    private Long categorieProduitId;
    private boolean modeEditionProduit = false;

    // Formulaire Catégorie
    private Categorie categorieForm = new Categorie();
    private boolean modeEditionCategorie = false;

    public String changerOnglet(String onglet) {
        this.ongletActif = onglet;
        return null;
    }

    // --- CRUD Produits ---
    public String preparerNouveauProduit() {
        this.produitForm = new Produit();
        this.produitForm.setQuantiteStock(10);
        this.produitForm.setEnVedette(false);
        List<Categorie> cats = categorieDAO.listerTout();
        this.categorieProduitId = cats.isEmpty() ? null : cats.get(0).getId();
        this.modeEditionProduit = false;
        return null;
    }

    public String editerProduit(Produit p) {
        this.produitForm = produitDAO.trouverParId(p.getId());
        this.categorieProduitId = (produitForm != null && produitForm.getCategorie() != null)
                ? produitForm.getCategorie().getId() : null;
        this.modeEditionProduit = true;
        this.ongletActif = "PRODUITS";
        return null;
    }

    public String enregistrerProduit() {
        try {
            if (categorieProduitId != null) {
                Categorie cat = categorieDAO.trouverParId(categorieProduitId);
                produitForm.setCategorie(cat);
            }
            if (modeEditionProduit && produitForm.getId() != null) {
                produitDAO.modifier(produitForm);
                ajouterMessage(FacesMessage.SEVERITY_INFO, "Produit modifié avec succès.");
            } else {
                produitDAO.ajouter(produitForm);
                ajouterMessage(FacesMessage.SEVERITY_INFO, "Nouveau produit ajouté au catalogue.");
            }
            preparerNouveauProduit();
            rafraichirVitrine();
        } catch (Exception e) {
            ajouterMessage(FacesMessage.SEVERITY_ERROR, "Erreur lors de l'enregistrement : " + e.getMessage());
        }
        return null;
    }

    public String supprimerProduit(Long id) {
        try {
            produitDAO.supprimer(id);
            ajouterMessage(FacesMessage.SEVERITY_INFO, "Produit supprimé du catalogue.");
            rafraichirVitrine();
        } catch (Exception e) {
            ajouterMessage(FacesMessage.SEVERITY_WARN, e.getMessage());
        }
        return null;
    }

    // --- CRUD Catégories ---
    public String preparerNouvelleCategorie() {
        this.categorieForm = new Categorie();
        this.categorieForm.setIcone("laptop");
        this.modeEditionCategorie = false;
        return null;
    }

    public String editerCategorie(Categorie c) {
        this.categorieForm = categorieDAO.trouverParId(c.getId());
        this.modeEditionCategorie = true;
        this.ongletActif = "CATEGORIES";
        return null;
    }

    public String enregistrerCategorie() {
        try {
            if (modeEditionCategorie && categorieForm.getId() != null) {
                categorieDAO.modifier(categorieForm);
                ajouterMessage(FacesMessage.SEVERITY_INFO, "Catégorie mise à jour.");
            } else {
                categorieDAO.ajouter(categorieForm);
                ajouterMessage(FacesMessage.SEVERITY_INFO, "Nouvelle catégorie créée.");
            }
            preparerNouvelleCategorie();
            rafraichirVitrine();
        } catch (Exception e) {
            ajouterMessage(FacesMessage.SEVERITY_ERROR, "Erreur : " + e.getMessage());
        }
        return null;
    }

    public String supprimerCategorie(Long id) {
        try {
            categorieDAO.supprimer(id);
            ajouterMessage(FacesMessage.SEVERITY_INFO, "Catégorie supprimée.");
            rafraichirVitrine();
        } catch (Exception e) {
            ajouterMessage(FacesMessage.SEVERITY_WARN, e.getMessage());
        }
        return null;
    }

    // --- Gestion des Commandes ---
    public String changerStatutCommande(Long commandeId, String nouveauStatut) {
        try {
            commandeDAO.changerStatut(commandeId, nouveauStatut);
            rafraichirVitrine();
            ajouterMessage(FacesMessage.SEVERITY_INFO, "Statut de la commande mis à jour vers : " + nouveauStatut);
        } catch (Exception e) {
            ajouterMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
        return null;
    }

    private void rafraichirVitrine() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (ctx == null) return;
        VitrineBean vb = ctx.getApplication().evaluateExpressionGet(ctx, "#{vitrineBean}", VitrineBean.class);
        if (vb != null) {
            vb.rafraichirCatalogue();
        }
    }

    private void ajouterMessage(FacesMessage.Severity severity, String texte) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (ctx != null) {
            ctx.addMessage(null, new FacesMessage(severity, texte, null));
        }
    }

    // --- Données pour les tableaux et KPIs ---
    public List<Produit> getTousLesProduits() {
        return produitDAO.listerTout();
    }

    public List<Categorie> getToutesLesCategories() {
        return categorieDAO.listerTout();
    }

    public List<Commande> getToutesLesCommandes() {
        return commandeDAO.listerTout();
    }

    public List<Internaute> getTousLesInternautes() {
        return internauteDAO.listerTout();
    }

    public long getNbProduits() {
        return produitDAO.compterProduits();
    }

    public long getNbCommandes() {
        return commandeDAO.compterCommandes();
    }

    public long getNbInternautes() {
        return internauteDAO.compterInternautes();
    }

    public double getChiffreAffaires() {
        return commandeDAO.calculerChiffreAffaires();
    }

    public String getOngletActif() {
        return ongletActif;
    }

    public void setOngletActif(String ongletActif) {
        this.ongletActif = ongletActif;
    }

    public Produit getProduitForm() {
        return produitForm;
    }

    public void setProduitForm(Produit produitForm) {
        this.produitForm = produitForm;
    }

    public Long getCategorieProduitId() {
        return categorieProduitId;
    }

    public void setCategorieProduitId(Long categorieProduitId) {
        this.categorieProduitId = categorieProduitId;
    }

    public boolean isModeEditionProduit() {
        return modeEditionProduit;
    }

    public void setModeEditionProduit(boolean modeEditionProduit) {
        this.modeEditionProduit = modeEditionProduit;
    }

    public Categorie getCategorieForm() {
        return categorieForm;
    }

    public void setCategorieForm(Categorie categorieForm) {
        this.categorieForm = categorieForm;
    }

    public boolean isModeEditionCategorie() {
        return modeEditionCategorie;
    }

    public void setModeEditionCategorie(boolean modeEditionCategorie) {
        this.modeEditionCategorie = modeEditionCategorie;
    }
}
