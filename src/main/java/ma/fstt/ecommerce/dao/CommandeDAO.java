package ma.fstt.ecommerce.dao;

import ma.fstt.ecommerce.entities.*;
import ma.fstt.ecommerce.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CommandeDAO implements Serializable {

    private static final long serialVersionUID = 1L;

    public Commande passerCommande(Long internauteId, List<LignePanier> lignes,
                                   String adresseLivraison, String villeLivraison,
                                   String telephoneContact, String modePaiement) {
        if (lignes == null || lignes.isEmpty()) {
            throw new IllegalArgumentException("Votre panier est vide.");
        }

        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            Internaute internaute = em.find(Internaute.class, internauteId);
            if (internaute == null) {
                throw new IllegalArgumentException("Internaute introuvable. Veuillez vous reconnecter.");
            }

            Commande commande = new Commande();
            String dateTag = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String randSuffix = String.format("%04d", (int) (Math.random() * 9000) + 1000);
            commande.setNumeroCommande("CMD-" + dateTag + "-" + randSuffix);
            commande.setDateCommande(new Date());
            commande.setStatut("VALIDEE");
            commande.setInternaute(internaute);
            commande.setAdresseLivraison(adresseLivraison);
            commande.setVilleLivraison(villeLivraison);
            commande.setTelephoneContact(telephoneContact);
            commande.setModePaiement(modePaiement != null ? modePaiement : "PAIEMENT_LIVRAISON");

            double sousTotal = 0.0;
            for (LignePanier lp : lignes) {
                Produit produitManage = em.find(Produit.class, lp.getProduit().getId());
                if (produitManage == null) {
                    throw new IllegalStateException("Un produit du panier n'existe plus.");
                }
                if (produitManage.getQuantiteStock() < lp.getQuantite()) {
                    throw new IllegalStateException("Stock insuffisant pour \"" + produitManage.getLibelle()
                            + "\" (stock actuel : " + produitManage.getQuantiteStock() + ").");
                }

                // Décrémentation atomique du stock produit dans la même transaction JPA
                produitManage.setQuantiteStock(produitManage.getQuantiteStock() - lp.getQuantite());

                LigneCommande lc = new LigneCommande(
                        commande, produitManage, lp.getQuantite(), produitManage.getPrix()
                );
                commande.getLignesCommande().add(lc);
                sousTotal += lc.getSousTotal();
            }

            double fraisLiv = sousTotal >= 1000.0 ? 0.0 : 49.0;
            commande.setFraisLivraison(fraisLiv);
            commande.setMontantTotal(Math.round((sousTotal + fraisLiv) * 100.0) / 100.0);

            em.persist(commande);

            // Vidage du panier persistant de l'internaute dans la même transaction
            List<Panier> paniers = em.createQuery(
                    "SELECT p FROM Panier p WHERE p.internaute.id = :iid", Panier.class)
                    .setParameter("iid", internauteId)
                    .getResultList();
            if (!paniers.isEmpty()) {
                Panier p = paniers.get(0);
                for (LignePanier item : p.getLignes()) {
                    em.remove(item);
                }
                p.getLignes().clear();
                p.setDateModification(new Date());
            }

            tx.commit();
            return commande;
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Commande> listerParInternaute(Long internauteId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createNamedQuery("Commande.findByInternaute", Commande.class)
                    .setParameter("internauteId", internauteId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Commande> listerTout() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createNamedQuery("Commande.findAll", Commande.class).getResultList();
        } finally {
            em.close();
        }
    }

    public Commande trouverParId(Long id) {
        if (id == null) return null;
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Commande.class, id);
        } finally {
            em.close();
        }
    }

    public void changerStatut(Long commandeId, String nouveauStatut) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Commande cmd = em.find(Commande.class, commandeId);
            if (cmd != null) {
                String ancienStatut = cmd.getStatut();
                if ("ANNULEE".equals(nouveauStatut) && !"ANNULEE".equals(ancienStatut)) {
                    // Restituer le stock si la commande est annulée
                    for (LigneCommande lc : cmd.getLignesCommande()) {
                        Produit p = em.find(Produit.class, lc.getProduit().getId());
                        if (p != null) {
                            p.setQuantiteStock(p.getQuantiteStock() + lc.getQuantite());
                        }
                    }
                }
                cmd.setStatut(nouveauStatut);
            }
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public long compterCommandes() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(c) FROM Commande c", Long.class).getSingleResult();
        } finally {
            em.close();
        }
    }

    public double calculerChiffreAffaires() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Double sum = em.createQuery(
                    "SELECT SUM(c.montantTotal) FROM Commande c WHERE c.statut <> 'ANNULEE'", Double.class)
                    .getSingleResult();
            return sum != null ? Math.round(sum * 100.0) / 100.0 : 0.0;
        } finally {
            em.close();
        }
    }
}
