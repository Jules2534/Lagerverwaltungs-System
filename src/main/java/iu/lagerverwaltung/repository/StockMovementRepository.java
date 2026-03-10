package iu.lagerverwaltung.repository;

import iu.lagerverwaltung.stockmovement.StockMovement;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

@ApplicationScoped
public class StockMovementRepository {

    private final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("lagerverwaltungssystemPU");

    public List<StockMovement> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT sm FROM StockMovement sm ORDER BY sm.timestamp DESC",
                    StockMovement.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    public void save(StockMovement stockMovement) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(stockMovement);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}