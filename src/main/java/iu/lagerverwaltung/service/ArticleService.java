package iu.lagerverwaltung.service;

import iu.lagerverwaltung.article.Article;
import iu.lagerverwaltung.dto.ArticleDTO;
import iu.lagerverwaltung.repository.ArticleRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ArticleService {

    @Inject
    private ArticleRepository articleRepository;

    public List<ArticleDTO> findAll() {
        return articleRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public void createArticle(ArticleDTO dto) {
        Article article = new Article(
                dto.getName(),
                dto.getArticleNumber(),
                dto.getDescription(),
                dto.getStock(),
                dto.getMinStock()
        );

        articleRepository.save(article);
    }

    private ArticleDTO toDTO(Article article) {
        ArticleDTO dto = new ArticleDTO();
        dto.setId(article.getId());
        dto.setName(article.getName());
        dto.setArticleNumber(article.getArticleNumber());
        dto.setDescription(article.getDescription());
        dto.setStock(article.getStock());
        dto.setMinStock(article.getMinStock());
        return dto;
    }
}