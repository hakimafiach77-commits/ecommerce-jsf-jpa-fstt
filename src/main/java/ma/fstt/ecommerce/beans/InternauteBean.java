package ma.fstt.ecommerce.beans;

import ma.fstt.ecommerce.dao.CommandeDAO;
import ma.fstt.ecommerce.dao.InternauteDAO;
import ma.fstt.ecommerce.entities.Commande;
import ma.fstt.ecommerce.entities.Internaute;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@ManagedBean(name = "internauteBean")
@SessionScoped
public class InternauteBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private final InternauteDAO internauteDAO = new InternauteDAO();
    private final CommandeDAO commandeDAO = new CommandeDAO();

    private Internaute internauteConnecte;

    // Formulaire de connexion
    private String emailConnexion;
    private String motDePasseConnexion;

    // Formulaire d'inscription
    private Internaute nouvelInternaute = new Internaute();
    private String motDePasseInscription;
    private String confirmationMotDePasse;

    // Commande sélectionnée dans l'historique
    private Commande commandeDetail;

    public boolean isConnecte() {
        return internauteConnecte != null;
    }

    public boolean isAdmin() {
        return internauteConnecte != null && internauteConnecte.isAdmin();
    }

    public String seConnecter() {
        if (emailConnexion == null || emailConnexion.trim().isEmpty()
                || motDePasseConnexion == null || motDePasseConnexion.isEmpty()) {
            ajouterMessage(FacesMessage.SEVERITY_ERROR, "Veuillez saisir votre e-mail et votre mot de passe.");
            return null;
        }

        Internaute auth = internauteDAO.authentifier(emailConnexion.trim(), motDePasseConnexion);
        if (auth == null) {
            ajouterMessage(FacesMessage.SEVERITY_ERROR, "Identifiants incorrects. Vérifiez votre e-mail ou mot de passe.");
            return null;
        }

        finaliserConnexion(auth);
        motDePasseConnexion = null;
        garderMessagesApresRedirect();
        ajouterMessage(FacesMessage.SEVERITY_INFO, "Bienvenue, " + auth.getNomComplet() + " !");
        return auth.isAdmin() ? "/admin.xhtml?faces-redirect=true" : "/index.xhtml?faces-redirect=true";
    }

    public String connexionRapideClient() {
        this.emailConnexion = "client@fstt.ac.ma";
        this.motDePasseConnexion = "client123";
        return seConnecter();
    }

    public String connexionRapideAdmin() {
        this.emailConnexion = "admin@fstt.ac.ma";
        this.motDePasseConnexion = "admin123";
        return seConnecter();
    }

    public String inscrire() {
        if (motDePasseInscription == null || motDePasseInscription.length() < 6) {
            ajouterMessage(FacesMessage.SEVERITY_ERROR, "Le mot de passe doit contenir au moins 6 caractères.");
            return null;
        }
        if (!motDePasseInscription.equals(confirmationMotDePasse)) {
            ajouterMessage(FacesMessage.SEVERITY_ERROR, "Les deux mots de passe ne correspondent pas.");
            return null;
        }

        try {
            nouvelInternaute.setRole("CLIENT");
            Internaute cree = internauteDAO.inscrire(nouvelInternaute, motDePasseInscription);
            finaliserConnexion(cree);

            nouvelInternaute = new Internaute();
            motDePasseInscription = null;
            confirmationMotDePasse = null;

            garderMessagesApresRedirect();
            ajouterMessage(FacesMessage.SEVERITY_INFO,
                    "Compte créé avec succès ! Bienvenue " + cree.getNomComplet() + ".");
            return "/index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            ajouterMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
            return null;
        }
    }

    public String mettreAJourProfil() {
        if (!isConnecte()) {
            return "/connexion.xhtml?faces-redirect=true";
        }
        try {
            this.internauteConnecte = internauteDAO.modifierProfil(internauteConnecte);
            PanierBean pb = obtenirPanierBean();
            if (pb != null) {
                pb.preremplirCoordonnees(internauteConnecte);
            }
            ajouterMessage(FacesMessage.SEVERITY_INFO, "Vos informations personnelles ont été mises à jour.");
        } catch (Exception e) {
            ajouterMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
        return null;
    }

    public String seDeconnecter() {
        this.internauteConnecte = null;
        this.commandeDetail = null;
        PanierBean pb = obtenirPanierBean();
        if (pb != null) {
            pb.reinitialiserApresDeconnexion();
        }
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (ctx != null) {
            ctx.getExternalContext().invalidateSession();
        }
        return "/index.xhtml?faces-redirect=true";
    }

    public List<Commande> getMesCommandes() {
        if (!isConnecte()) {
            return Collections.emptyList();
        }
        return commandeDAO.listerParInternaute(internauteConnecte.getId());
    }

    public String voirFactureCommande(Commande cmd) {
        PanierBean pb = obtenirPanierBean();
        if (pb != null) {
            pb.setDerniereCommande(cmd);
        }
        return "/commande-confirmation.xhtml?faces-redirect=true";
    }

    private void finaliserConnexion(Internaute internaute) {
        this.internauteConnecte = internaute;
        PanierBean pb = obtenirPanierBean();
        if (pb != null) {
            pb.synchroniserApresConnexion(internaute);
        }
    }

    private PanierBean obtenirPanierBean() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (ctx == null) return null;
        return ctx.getApplication().evaluateExpressionGet(ctx, "#{panierBean}", PanierBean.class);
    }

    private void garderMessagesApresRedirect() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (ctx != null) {
            ctx.getExternalContext().getFlash().setKeepMessages(true);
        }
    }

    private void ajouterMessage(FacesMessage.Severity severity, String texte) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (ctx != null) {
            ctx.addMessage(null, new FacesMessage(severity, texte, null));
        }
    }

    public Internaute getInternauteConnecte() {
        return internauteConnecte;
    }

    public void setInternauteConnecte(Internaute internauteConnecte) {
        this.internauteConnecte = internauteConnecte;
    }

    public String getEmailConnexion() {
        return emailConnexion;
    }

    public void setEmailConnexion(String emailConnexion) {
        this.emailConnexion = emailConnexion;
    }

    public String getMotDePasseConnexion() {
        return motDePasseConnexion;
    }

    public void setMotDePasseConnexion(String motDePasseConnexion) {
        this.motDePasseConnexion = motDePasseConnexion;
    }

    public Internaute getNouvelInternaute() {
        return nouvelInternaute;
    }

    public void setNouvelInternaute(Internaute nouvelInternaute) {
        this.nouvelInternaute = nouvelInternaute;
    }

    public String getMotDePasseInscription() {
        return motDePasseInscription;
    }

    public void setMotDePasseInscription(String motDePasseInscription) {
        this.motDePasseInscription = motDePasseInscription;
    }

    public String getConfirmationMotDePasse() {
        return confirmationMotDePasse;
    }

    public void setConfirmationMotDePasse(String confirmationMotDePasse) {
        this.confirmationMotDePasse = confirmationMotDePasse;
    }

    public Commande getCommandeDetail() {
        return commandeDetail;
    }

    public void setCommandeDetail(Commande commandeDetail) {
        this.commandeDetail = commandeDetail;
    }
}
