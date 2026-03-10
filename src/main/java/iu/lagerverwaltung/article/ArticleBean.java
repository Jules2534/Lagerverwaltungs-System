package iu.lagerverwaltung.article;

import iu.lagerverwaltung.dto.ArticleDTO;
import iu.lagerverwaltung.service.ArticleService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;

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

    public String saveArticle() {
        articleService.createArticle(newArticle);
        return "articles?faces-redirect=true";
    }
}