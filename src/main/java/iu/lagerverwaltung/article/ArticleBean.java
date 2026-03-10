package iu.lagerverwaltung.article;

import iu.lagerverwaltung.dto.ArticleDTO;
import iu.lagerverwaltung.service.ArticleService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;
import java.util.stream.Collectors;

@Named
@RequestScoped
public class ArticleBean {

    @Inject
    private ArticleService articleService;

    private ArticleDTO newArticle = new ArticleDTO();

    public ArticleDTO getNewArticle() {
        return newArticle;
    }

    public void setNewArticle(ArticleDTO newArticle) {
        this.newArticle = newArticle;
    }

    public List<ArticleDTO> getArticles() {
        return articleService.findAll();
    }

    public List<ArticleDTO> getLowStockArticles() {
        return getArticles().stream()
                .filter(article -> article.getStock() < article.getMinStock())
                .collect(Collectors.toList());
    }

    public int getLowStockCount() {
        return getLowStockArticles().size();
    }

    public boolean isBelowMinimum(ArticleDTO article) {
        return article.getStock() < article.getMinStock();
    }

    public String saveArticle() {
        articleService.createArticle(newArticle);
        return "articles?faces-redirect=true";
    }
}