package iu.lagerverwaltung.stockmovement;

import iu.lagerverwaltung.article.Article;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "stock_movements")
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "article_id")
    private Article article;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false)
    private MovementType movementType;

    private int quantity;

    private String note;

    private LocalDateTime timestamp;

    public StockMovement() {
    }

    public StockMovement(Article article, MovementType movementType, int quantity, String note, LocalDateTime timestamp) {
        this.article = article;
        this.movementType = movementType;
        this.quantity = quantity;
        this.note = note;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public Article getArticle() {
        return article;
    }

    public MovementType getMovementType() {
        return movementType;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getNote() {
        return note;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public void setMovementType(MovementType movementType) {
        this.movementType = movementType;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}