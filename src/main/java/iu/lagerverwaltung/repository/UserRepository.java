package iu.lagerverwaltung.repository;

import iu.lagerverwaltung.usermanagement.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;

@ApplicationScoped
public class UserRepository {

    private final EntityManager em = Persistence
            .createEntityManagerFactory("lagerverwaltungssystemPU")
            .createEntityManager();

    public void save(User user) {
        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            throw e; // ganz wichtig: Fehler weitergeben
        }
    }

    public boolean emailExists(String email) {
        Long count = em.createQuery(
                        "SELECT COUNT(u) FROM User u WHERE u.email = :email",
                        Long.class
                ).setParameter("email", email)
                .getSingleResult();

        return count > 0;
    }

    public User findByUsername(String username) {
        try {
            return em.createQuery(
                            "SELECT u FROM User u WHERE u.username = :username",
                            User.class
                    ).setParameter("username", username)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }

    public User findById(Long id) {
        return em.find(User.class, id);
    }

    public boolean usernameExists(String username) {
        Long count = em.createQuery(
                        "SELECT COUNT(u) FROM User u WHERE u.username = :username",
                        Long.class
                ).setParameter("username", username)
                .getSingleResult();

        return count > 0;
    }
}