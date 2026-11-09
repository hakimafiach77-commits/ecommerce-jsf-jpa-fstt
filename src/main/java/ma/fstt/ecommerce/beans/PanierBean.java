package ma.fstt.ecommerce.beans;

import ma.fstt.ecommerce.dao.CommandeDAO;
import ma.fstt.ecommerce.dao.PanierDAO;
import ma.fstt.ecommerce.dao.ProduitDAO;
import ma.fstt.ecommerce.entities.*;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@ManagedBean(name = "panierBean")
@SessionScoped
public class PanierBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private final PanierDAO panierDAO = new PanierDAO();
    private final ProduitDAO produitDAO = new ProduitDAO();
    private final CommandeDAO commandeDAO = new CommandeDAO();

    // Panier utilisé en mode invité ou synchronisé avec la BDD en mode connecté
    private Panier panierActif = new Panier();

    // Champs du formulaire de validation de commande
    private String adresseLivraison;
    private String villeLivraison;
    private String telephoneContact;
    private String modePaiement = "PAIEMENT_LIVRAISON";

    private Commande derniereCommande;

    private Internaute getInternauteConnecte() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (ctx == null) return null;
        InternauteBean ib = ctx.getApplication().evaluateExpressionGet(ctx, "#{internauteBean}", InternauteBean.class);
        return (ib != null) ? ib.getInternauteConnecte() : null;
    }

    private void rafraichirVitrine() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (ctx == null) return;
        VitrineBean vb = ctx.getApplication().evaluateExpressionGet(ctx, "#{vitrineBean}", VitrineBean.class);
        if (vb != null) {
            vb.rafraichirCatalogue();
        }
    }

    public void synchroniserApresConnexion(Internaute internaute) {
        if (internaute == null) return;
        List<LignePanier> inviteLignes = new ArrayList<>(panierActif.getLignes());
        this.panierActif = panierDAO.fusionnerPanierInvite(internaute.getId(), inviteLignes);
        if (this.panierActif == null) {
            this.panierActif = new Panier(internaute);
        }
        preremplirCoordonnees(internaute);
    }

    public void reinitialiserApresDeconnexion() {
        this.panierActif = new Panier();
        this.adresseLivraison = null;
        this.villeLivraison = null;
        this.telephoneContact = null;
        this.modePaiement = "PAIEMENT_LIVRAISON";
    }

    public void preremplirCoordonnees(Internaute internaute) {
        if (internaute != null) {
            this.adresseLivraison = internaute.getAdresse();
            this.villeLivraison = internaute.getVille();
            this.telephoneContact = internaute.getTelephone();
        }
    }

    public String ajouterAuPanier(Long produitId) {
        return ajouterAvecQuantite(produitId, 1);
    }

    public String ajouterDepuisDetail() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        VitrineBean vb = ctx.getApplication().evaluateExpressionGet(ctx, "#{vitrineBean}", VitrineBean.class);
        if (vb != null && vb.getProduitSelectionne() != null) {
            int qte = Math.max(1, vb.getQuantiteDetail());
            return ajouterAvecQuantite(vb.getProduitSelectionne().getId(), qte);
        }
        return null;
    }

    public String ajouterAvecQuantite(Long produitId, int quantite) {
        try {
            if (quantite <= 0) quantite = 1;
            Produit produit = produitDAO.trouverParId(produitId);
            if (produit == null) {
                ajouterMessage(FacesMessage.SEVERITY_ERROR, "Produit introuvable.");
                return null;
            }

            Internaute connecte = getInternauteConnecte();
            if (connecte != null) {
                this.panierActif = panierDAO.ajouterOuModifierArticle(connecte.getId(), produitId, quantite);
            } else {
                LignePanier existante = null;
                for (LignePanier lp : panierActif.getLignes()) {
                    if (lp.getProduit().getId().equals(produitId)) {
                        existante = lp;
                        break;
                    }
                }
                int nouvelleQte = (existante != null ? existante.getQuantite() : 0) + quantite;
                if (nouvelleQte > produit.getQuantiteStock()) {
                    ajouterMessage(FacesMessage.SEVERITY_WARN,
                            "Stock insuffisant pour \"" + produit.getLibelle() + "\" (disponible : " + produit.getQuantiteStock() + ").");
                    return null;
                }
                if (existante != null) {
                    existante.setQuantite(nouvelleQte);
                    existante.setPrixUnitaire(produit.getPrix());
                } else {
                    panierActif.getLignes().add(new LignePanier(panierActif, produit, nouvelleQte));
                }
            }

            ajouterMessage(FacesMessage.SEVERITY_INFO,
                    "\"" + produit.getLibelle() + "\" a été ajouté à votre panier.");
        } catch (Exception e) {
            ajouterMessage(FacesMessage.SEVERITY_WARN, e.getMessage());
        }
        return null;
    }

    public String incrementer(Long produitId) {
        return ajouterAvecQuantite(produitId, 1);
    }

    public String decrementer(Long produitId) {
        try {
            Internaute connecte = getInternauteConnecte();
            if (connecte != null) {
                this.panierActif = panierDAO.ajouterOuModifierArticle(connecte.getId(), produitId, -1);
            } else {
                Iterator<LignePanier> it = panierActif.getLignes().iterator();
                while (it.hasNext()) {
                    LignePanier lp = it.next();
                    if (lp.getProduit().getId().equals(produitId)) {
                        if (lp.getQuantite() <= 1) {
                            it.remove();
                        } else {
                            lp.setQuantite(lp.getQuantite() - 1);
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {
            ajouterMessage(FacesMessage.SEVERITY_WARN, e.getMessage());
        }
        return null;
    }

    public String supprimerArticle(Long produitId) {
        try {
            Internaute connecte = getInternauteConnecte();
            if (connecte != null) {
                Panier p = panierDAO.supprimerLigne(connecte.getId(), produitId);
                this.panierActif = (p != null) ? p : new Panier(connecte);
            } else {
                panierActif.getLignes().removeIf(lp -> lp.getProduit().getId().equals(produitId));
            }
            ajouterMessage(FacesMessage.SEVERITY_INFO, "Article retiré du panier.");
        } catch (Exception e) {
            ajouterMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
        return null;
    }

    public String viderPanier() {
        try {
            Internaute connecte = getInternauteConnecte();
            if (connecte != null) {
                Panier p = panierDAO.viderPanier(connecte.getId());
                this.panierActif = (p != null) ? p : new Panier(connecte);
            } else {
                panierActif.getLignes().clear();
            }
            ajouterMessage(FacesMessage.SEVERITY_INFO, "Votre panier a été vidé.");
        } catch (Exception e) {
            ajouterMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
        return null;
    }

    public String validerCommande() {
        Internaute connecte = getInternauteConnecte();
        if (connecte == null) {
            ajouterMessage(FacesMessage.SEVERITY_WARN,
                    "Veuillez vous connecter ou créer un compte pour finaliser votre commande.");
            return "/connexion.xhtml?faces-redirect=true";
        }
        if (panierActif == null || panierActif.getLignes().isEmpty()) {
            ajouterMessage(FacesMessage.SEVERITY_WARN, "Votre panier est vide.");
            return null;
        }
        if (adresseLivraison == null || adresseLivraison.trim().isEmpty()) {
            adresseLivraison = connecte.getAdresse();
        }
        if (villeLivraison == null || villeLivraison.trim().isEmpty()) {
            villeLivraison = connecte.getVille();
        }
        if (telephoneContact == null || telephoneContact.trim().isEmpty()) {
            telephoneContact = connecte.getTelephone();
        }
        if (adresseLivraison == null || adresseLivraison.trim().isEmpty()
                || villeLivraison == null || villeLivraison.trim().isEmpty()) {
            ajouterMessage(FacesMessage.SEVERITY_ERROR,
                    "Veuillez renseigner l'adresse et la ville de livraison.");
            return null;
        }

        try {
            Commande cmd = commandeDAO.passerCommande(
                    connecte.getId(),
                    new ArrayList<>(panierActif.getLignes()),
                    adresseLivraison.trim(),
                    villeLivraison.trim(),
                    telephoneContact != null ? telephoneContact.trim() : "",
                    modePaiement
            );
            this.derniereCommande = cmd;
            this.panierActif = panierDAO.obtenirOuCreerPanier(connecte.getId());
            rafraichirVitrine();
            return "/commande-confirmation.xhtml?faces-redirect=true";
        } catch (Exception e) {
            ajouterMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
            return null;
        }
    }

    private void ajouterMessage(FacesMessage.Severity severity, String texte) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (ctx != null) {
            ctx.addMessage(null, new FacesMessage(severity, texte, null));
        }
    }

    public List<LignePanier> getLignes() {
        return panierActif != null ? panierActif.getLignes() : new ArrayList<>();
    }

    public boolean isVide() {
        return getLignes().isEmpty();
    }

    public int getNombreArticles() {
        return panierActif != null ? panierActif.getNombreArticles() : 0;
    }

    public double getSousTotalTTC() {
        return panierActif != null ? panierActif.getSousTotalTTC() : 0.0;
    }

    public double getTotalHT() {
        return panierActif != null ? panierActif.getTotalHT() : 0.0;
    }

    public double getMontantTVA() {
        return panierActif != null ? panierActif.getMontantTVA() : 0.0;
    }

    public double getFraisLivraison() {
        return panierActif != null ? panierActif.getFraisLivraison() : 0.0;
    }

    public double getTotalGeneral() {
        return panierActif != null ? panierActif.getTotalGeneral() : 0.0;
    }

    public double getMontantRestantLivraisonGratuite() {
        double reste = 1000.0 - getSousTotalTTC();
        return reste > 0 ? Math.round(reste * 100.0) / 100.0 : 0.0;
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

    public Commande getDerniereCommande() {
        return derniereCommande;
    }

    public void setDerniereCommande(Commande derniereCommande) {
        this.derniereCommande = derniereCommande;
    }
}
