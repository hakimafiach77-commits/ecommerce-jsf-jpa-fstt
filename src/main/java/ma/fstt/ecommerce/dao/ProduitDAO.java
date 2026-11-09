package ma.fstt.ecommerce.dao;

import ma.fstt.ecommerce.entities.Produit;
import ma.fstt.ecommerce.util.JPAUtil;
import ma.fstt.ecommerce.util.ProductImageGenerator;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ProduitDAO implements Serializable {

    private static final long serialVersionUID = 1L;

    public List<Produit> listerTout() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createNamedQuery("Produit.findAll", Produit.class).getResultList();
        } finally {
            em.close();
        }
    }

    public Produit trouverParId(Long id) {
        if (id == null) return null;
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Produit.class, id);
        } finally {
            em.close();
        }
    }

    public List<Produit> rechercherEtFiltrer(Long categorieId, String motCle, String tri) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            StringBuilder jpql = new StringBuilder("SELECT p FROM Produit p WHERE 1=1 ");
            if (categorieId != null && categorieId > 0) {
                jpql.append("AND p.categorie.id = :catId ");
            }
            boolean hasKeyword = motCle != null && !motCle.trim().isEmpty();
            if (hasKeyword) {
                jpql.append("AND (LOWER(p.libelle) LIKE :kw OR LOWER(p.marque) LIKE :kw OR LOWER(p.description) LIKE :kw) ");
            }

            if ("PRIX_ASC".equals(tri)) {
                jpql.append("ORDER BY p.prix ASC");
            } else if ("PRIX_DESC".equals(tri)) {
                jpql.append("ORDER BY p.prix DESC");
            } else if ("NOUVEAUTES".equals(tri)) {
                jpql.append("ORDER BY p.dateAjout DESC, p.id DESC");
            } else {
                jpql.append("ORDER BY p.enVedette DESC, p.id DESC");
            }

            TypedQuery<Produit> query = em.createQuery(jpql.toString(), Produit.class);
            if (categorieId != null && categorieId > 0) {
                query.setParameter("catId", categorieId);
            }
            if (hasKeyword) {
                query.setParameter("kw", "%" + motCle.trim().toLowerCase() + "%");
            }
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Produit> listerSimilaires(Long categorieId, Long excludeProduitId, int maxResults) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT p FROM Produit p WHERE p.categorie.id = :cid AND p.id <> :pid ORDER BY p.enVedette DESC, p.id DESC",
                    Produit.class)
                    .setParameter("cid", categorieId)
                    .setParameter("pid", excludeProduitId)
                    .setMaxResults(maxResults)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public Produit ajouter(Produit produit) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (produit.getDateAjout() == null) {
                produit.setDateAjout(new Date());
            }
            if (produit.getImageUrl() == null || produit.getImageUrl().trim().isEmpty()) {
                String icone = (produit.getCategorie() != null && produit.getCategorie().getIcone() != null)
                        ? produit.getCategorie().getIcone() : "laptop";
                String badge = produit.getMarque() != null ? produit.getMarque().toUpperCase() : "ATLASTECH";
                produit.setImageUrl(ProductImageGenerator.createSvgDataUri(icone, badge, "#1e3a8a", "#0f766e"));
            }
            em.persist(produit);
            tx.commit();
            return produit;
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public Produit modifier(Produit produit) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (produit.getImageUrl() == null || produit.getImageUrl().trim().isEmpty()) {
                String icone = (produit.getCategorie() != null && produit.getCategorie().getIcone() != null)
                        ? produit.getCategorie().getIcone() : "laptop";
                String badge = produit.getMarque() != null ? produit.getMarque().toUpperCase() : "ATLASTECH";
                produit.setImageUrl(ProductImageGenerator.createSvgDataUri(icone, badge, "#1e3a8a", "#0f766e"));
            }
            Produit merged = em.merge(produit);
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
            Long nbCommandes = em.createQuery(
                    "SELECT COUNT(lc) FROM LigneCommande lc WHERE lc.produit.id = :pid", Long.class)
                    .setParameter("pid", id)
                    .getSingleResult();
            if (nbCommandes != null && nbCommandes > 0) {
                throw new IllegalStateException("Ce produit figure déjà dans des commandes clients. Mettez son stock à 0 plutôt que de le supprimer.");
            }
            em.createQuery("DELETE FROM LignePanier lp WHERE lp.produit.id = :pid")
                    .setParameter("pid", id)
                    .executeUpdate();
            Produit p = em.find(Produit.class, id);
            if (p != null) {
                em.remove(p);
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

    public long compterProduits() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(p) FROM Produit p", Long.class).getSingleResult();
        } finally {
            em.close();
        }
    }
}
