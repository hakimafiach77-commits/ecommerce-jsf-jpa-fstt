package ma.fstt.ecommerce.dao;

import ma.fstt.ecommerce.entities.Categorie;
import ma.fstt.ecommerce.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.Serializable;
import java.util.List;

public class CategorieDAO implements Serializable {

    private static final long serialVersionUID = 1L;

    public List<Categorie> listerTout() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createNamedQuery("Categorie.findAll", Categorie.class).getResultList();
        } finally {
            em.close();
        }
    }

    public Categorie trouverParId(Long id) {
        if (id == null) return null;
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Categorie.class, id);
        } finally {
            em.close();
        }
    }

    public Categorie ajouter(Categorie categorie) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(categorie);
            tx.commit();
            return categorie;
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public Categorie modifier(Categorie categorie) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Categorie merged = em.merge(categorie);
            tx.commit();
            return merged;
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void supprimer(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Long nbProduits = em.createQuery(
                    "SELECT COUNT(p) FROM Produit p WHERE p.categorie.id = :cid", Long.class)
                    .setParameter("cid", id)
                    .getSingleResult();
            if (nbProduits != null && nbProduits > 0) {
                throw new IllegalStateException("Impossible de supprimer cette catégorie car elle contient " + nbProduits + " produit(s).");
            }
            Categorie c = em.find(Categorie.class, id);
            if (c != null) {
                em.remove(c);
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
}
