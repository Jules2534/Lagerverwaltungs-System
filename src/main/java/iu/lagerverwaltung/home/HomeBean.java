package iu.lagerverwaltung.home;

import iu.lagerverwaltung.service.ArticleService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@RequestScoped
public class HomeBean {

    @Inject
    private ArticleService articleService;

    // Gesamtzahl der Artikel
    public long getTotalArticles() {
        return articleService.findAll().size();
    }

    // Anzahl der Artikel unter Mindestbestand
    public long getArticlesBelowMinimum() {
        return articleService.findAll().stream()
                .filter(article -> article.getStock() < article.getMinStock())
                .count();
    }

    // Anzahl der Artikel über Mindestbestand
    public long getArticlesAboveMinimum() {
        return articleService.findAll().stream()
                .filter(article -> article.getStock() >= article.getMinStock())
                .count();
    }
}