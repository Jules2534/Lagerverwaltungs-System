package iu.lagerverwaltung.repository;

import iu.lagerverwaltung.article.Article;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

@ApplicationScoped
public class ArticleRepository {

    private final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("lagerverwaltungssystemPU");

    public List<Article> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT a FROM Article a ORDER BY a.id", Article.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public Article findById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Article.class, id);
        } finally {
            em.close();
        }
    }

    public void save(Article article) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(article);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void update(Article article) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(article);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}