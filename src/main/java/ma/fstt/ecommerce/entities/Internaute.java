package ma.fstt.ecommerce.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "internaute")
@NamedQueries({
    @NamedQuery(name = "Internaute.findAll", query = "SELECT i FROM Internaute i ORDER BY i.dateInscription DESC"),
    @NamedQuery(name = "Internaute.findByEmail", query = "SELECT i FROM Internaute i WHERE LOWER(i.email) = LOWER(:email)")
})
public class Internaute implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String nom;

    @Column(nullable = false, length = 80)
    private String prenom;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "mot_de_passe", nullable = false, length = 255)
    private String motDePasse;

    @Column(length = 30)
    private String telephone;

    @Column(length = 350)
    private String adresse;

    @Column(length = 80)
    private String ville;

    @Column(nullable = false, length = 20)
    private String role = "CLIENT"; // CLIENT ou ADMIN

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_inscription")
    private Date dateInscription;

    @OneToOne(mappedBy = "internaute", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Panier panier;

    @OneToMany(mappedBy = "internaute", fetch = FetchType.LAZY)
    private List<Commande> commandes = new ArrayList<>();

    public Internaute() {
        this.dateInscription = new Date();
        this.role = "CLIENT";
    }

    public Internaute(String nom, String prenom, String email, String motDePasse,
                      String telephone, String adresse, String ville, String role) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.telephone = telephone;
        this.adresse = adresse;
        this.ville = ville;
        this.role = role != null ? role : "CLIENT";
        this.dateInscription = new Date();
    }

    public String getNomComplet() {
        return (prenom != null ? prenom : "") + " " + (nom != null ? nom : "");
    }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(this.role);
    }

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

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(Date dateInscription) {
        this.dateInscription = dateInscription;
    }

    public Panier getPanier() {
        return panier;
    }

    public void setPanier(Panier panier) {
        this.panier = panier;
    }

    public List<Commande> getCommandes() {
        return commandes;
    }

    public void setCommandes(List<Commande> commandes) {
        this.commandes = commandes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Internaute)) return false;
        Internaute that = (Internaute) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
