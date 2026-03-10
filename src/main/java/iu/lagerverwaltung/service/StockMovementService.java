package iu.lagerverwaltung.service;

import iu.lagerverwaltung.article.Article;
import iu.lagerverwaltung.dto.StockMovementDTO;
import iu.lagerverwaltung.repository.ArticleRepository;
import iu.lagerverwaltung.repository.StockMovementRepository;
import iu.lagerverwaltung.stockmovement.MovementType;
import iu.lagerverwaltung.stockmovement.StockMovement;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class StockMovementService {

    @Inject
    private StockMovementRepository stockMovementRepository;

    @Inject
    private ArticleRepository articleRepository;

    public List<StockMovementDTO> findAll() {
        return stockMovementRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public void createMovement(StockMovementDTO dto) {
        Article article = articleRepository.findById(dto.getArticleId());

        if (article == null) {
            throw new IllegalArgumentException("Artikel wurde nicht gefunden.");
        }

        MovementType type = MovementType.valueOf(dto.getMovementType());

        if (type == MovementType.IN) {
            article.setStock(article.getStock() + dto.getQuantity());
        } else {
            int newStock = article.getStock() - dto.getQuantity();
            if (newStock < 0) {
                throw new IllegalArgumentException("Bestand darf nicht negativ werden.");
            }
            article.setStock(newStock);
        }

        articleRepository.update(article);

        StockMovement stockMovement = new StockMovement(
                article,
                type,
                dto.getQuantity(),
                dto.getNote(),
                LocalDateTime.now()
        );

        stockMovementRepository.save(stockMovement);
    }

    private StockMovementDTO toDTO(StockMovement stockMovement) {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        StockMovementDTO dto = new StockMovementDTO();

        dto.setArticleId(stockMovement.getArticle().getId());
        dto.setArticleName(stockMovement.getArticle().getName());
        dto.setMovementType(stockMovement.getMovementType().name());
        dto.setQuantity(stockMovement.getQuantity());
        dto.setNote(stockMovement.getNote());

        dto.setTimestamp(
                stockMovement.getTimestamp().format(formatter)
        );

        return dto;
    }

    // Methode zum Abrufen der Bestandsbewegungen für einen bestimmten Artikel
    public List<StockMovementDTO> getStockReportForArticle(Long articleId) {
        return stockMovementRepository.findByArticleId(articleId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Methode zum Abrufen der Bestandsbewegungen mit niedrigem Bestand
    public List<StockMovementDTO> findLowStockMovements() {
        return stockMovementRepository.findAll().stream()
                .filter(sm -> sm.getArticle().getStock() < sm.getArticle().getMinStock())
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}