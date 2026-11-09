package ma.fstt.ecommerce.dao;

import ma.fstt.ecommerce.entities.Internaute;
import ma.fstt.ecommerce.entities.Panier;
import ma.fstt.ecommerce.util.JPAUtil;
import ma.fstt.ecommerce.util.PasswordUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class InternauteDAO implements Serializable {

    private static final long serialVersionUID = 1L;

    public List<Internaute> listerTout() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createNamedQuery("Internaute.findAll", Internaute.class).getResultList();
        } finally {
            em.close();
        }
    }

    public Internaute trouverParId(Long id) {
        if (id == null) return null;
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Internaute.class, id);
        } finally {
            em.close();
        }
    }

    public Internaute trouverParEmail(String email) {
        if (email == null || email.trim().isEmpty()) return null;
        EntityManager em = JPAUtil.getEntityManager();
        try {
            List<Internaute> res = em.createNamedQuery("Internaute.findByEmail", Internaute.class)
                    .setParameter("email", email.trim())
                    .getResultList();
            return res.isEmpty() ? null : res.get(0);
        } finally {
            em.close();
        }
    }

    public Internaute inscrire(Internaute internaute, String motDePasseClair) {
        if (trouverParEmail(internaute.getEmail()) != null) {
            throw new IllegalArgumentException("Un compte existe déjà avec l'adresse e-mail : " + internaute.getEmail());
        }

        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            internaute.setEmail(internaute.getEmail().trim().toLowerCase());
            internaute.setMotDePasse(PasswordUtil.hashPassword(motDePasseClair));
            internaute.setDateInscription(new Date());
            if (internaute.getRole() == null || internaute.getRole().isEmpty()) {
                internaute.setRole("CLIENT");
            }
            em.persist(internaute);

            Panier panier = new Panier(internaute);
            em.persist(panier);
            internaute.setPanier(panier);

            tx.commit();
            return internaute;
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public Internaute authentifier(String email, String motDePasseClair) {
        Internaute internaute = trouverParEmail(email);
        if (internaute == null) {
            return null;
        }
        if (PasswordUtil.verifyPassword(motDePasseClair, internaute.getMotDePasse())) {
            return internaute;
        }
        return null;
    }

    public Internaute modifierProfil(Internaute internaute) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Internaute existant = em.find(Internaute.class, internaute.getId());
            if (existant == null) {
                throw new IllegalArgumentException("Internaute introuvable.");
            }
            existant.setNom(internaute.getNom());
            existant.setPrenom(internaute.getPrenom());
            existant.setTelephone(internaute.getTelephone());
            existant.setAdresse(internaute.getAdresse());
            existant.setVille(internaute.getVille());
            tx.commit();
            return existant;
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public long compterInternautes() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(i) FROM Internaute i", Long.class).getSingleResult();
        } finally {
            em.close();
        }
    }
}
