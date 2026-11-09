package ma.fstt.ecommerce.dao;

import ma.fstt.ecommerce.entities.Internaute;
import ma.fstt.ecommerce.entities.LignePanier;
import ma.fstt.ecommerce.entities.Panier;
import ma.fstt.ecommerce.entities.Produit;
import ma.fstt.ecommerce.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class PanierDAO implements Serializable {

    private static final long serialVersionUID = 1L;

    public Panier obtenirOuCreerPanier(Long internauteId) {
        if (internauteId == null) return null;
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            List<Panier> paniers = em.createQuery(
                    "SELECT p FROM Panier p WHERE p.internaute.id = :iid", Panier.class)
                    .setParameter("iid", internauteId)
                    .getResultList();
            if (!paniers.isEmpty()) {
                Panier p = paniers.get(0);
                p.getLignes().size(); // Initialisation explicite
                return p;
            }
            tx.begin();
            Internaute internaute = em.find(Internaute.class, internauteId);
            if (internaute == null) {
                tx.rollback();
                return null;
            }
            Panier nouveau = new Panier(internaute);
            em.persist(nouveau);
            tx.commit();
            return nouveau;
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public Panier ajouterOuModifierArticle(Long internauteId, Long produitId, int deltaQuantite) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Produit produit = em.find(Produit.class, produitId);
            if (produit == null) {
                throw new IllegalArgumentException("Produit introuvable.");
            }

            List<Panier> paniers = em.createQuery(
                    "SELECT p FROM Panier p WHERE p.internaute.id = :iid", Panier.class)
                    .setParameter("iid", internauteId)
                    .getResultList();
            Panier panier;
            if (paniers.isEmpty()) {
                Internaute internaute = em.find(Internaute.class, internauteId);
                panier = new Panier(internaute);
                em.persist(panier);
            } else {
                panier = paniers.get(0);
            }

            LignePanier existante = null;
            for (LignePanier lp : panier.getLignes()) {
                if (lp.getProduit().getId().equals(produitId)) {
                    existante = lp;
                    break;
                }
            }

            int nouvelleQte = (existante != null ? existante.getQuantite() : 0) + deltaQuantite;
            if (nouvelleQte > produit.getQuantiteStock()) {
                throw new IllegalStateException("Stock insuffisant pour \"" + produit.getLibelle()
                        + "\" (disponible : " + produit.getQuantiteStock() + ").");
            }

            if (nouvelleQte <= 0) {
                if (existante != null) {
                    panier.getLignes().remove(existante);
                    em.remove(existante);
                }
            } else if (existante != null) {
                existante.setQuantite(nouvelleQte);
                existante.setPrixUnitaire(produit.getPrix());
            } else {
                LignePanier nouvelleLigne = new LignePanier(panier, produit, nouvelleQte);
                panier.getLignes().add(nouvelleLigne);
                em.persist(nouvelleLigne);
            }

            panier.setDateModification(new Date());
            tx.commit();
            return panier;
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public Panier supprimerLigne(Long internauteId, Long produitId) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            List<Panier> paniers = em.createQuery(
                    "SELECT p FROM Panier p WHERE p.internaute.id = :iid", Panier.class)
                    .setParameter("iid", internauteId)
                    .getResultList();
            if (paniers.isEmpty()) {
                tx.commit();
                return null;
            }
            Panier panier = paniers.get(0);
            Iterator<LignePanier> it = panier.getLignes().iterator();
            while (it.hasNext()) {
                LignePanier lp = it.next();
                if (lp.getProduit().getId().equals(produitId)) {
                    it.remove();
                    em.remove(lp);
                }
            }
            panier.setDateModification(new Date());
            tx.commit();
            return panier;
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public Panier viderPanier(Long internauteId) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            List<Panier> paniers = em.createQuery(
                    "SELECT p FROM Panier p WHERE p.internaute.id = :iid", Panier.class)
                    .setParameter("iid", internauteId)
                    .getResultList();
            if (!paniers.isEmpty()) {
                Panier panier = paniers.get(0);
                for (LignePanier lp : panier.getLignes()) {
                    em.remove(lp);
                }
                panier.getLignes().clear();
                panier.setDateModification(new Date());
                tx.commit();
                return panier;
            }
            tx.commit();
            return null;
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public Panier fusionnerPanierInvite(Long internauteId, List<LignePanier> lignesInvite) {
        if (lignesInvite != null) {
            for (LignePanier lp : lignesInvite) {
                if (lp.getProduit() != null && lp.getQuantite() != null && lp.getQuantite() > 0) {
                    try {
                        ajouterOuModifierArticle(internauteId, lp.getProduit().getId(), lp.getQuantite());
                    } catch (RuntimeException ignored) {
                        // Ignore un article si le stock est dépassé lors de la fusion
                    }
                }
            }
        }
        return obtenirOuCreerPanier(internauteId);
    }
}
