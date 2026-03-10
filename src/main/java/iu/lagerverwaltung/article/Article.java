package iu.lagerverwaltung.article;

import jakarta.persistence.*;

@Entity
@Table(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "article_number", unique = true, nullable = false)
    private String articleNumber;

    private String description;

    private int stock;

    @Column(name = "min_stock")
    private int minStock;

    public Article() {
    }

    public Article(String name, String articleNumber, String description, int stock, int minStock) {
        this.name = name;
        this.articleNumber = articleNumber;
        this.description = description;
        this.stock = stock;
        this.minStock = minStock;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getArticleNumber() {
        return articleNumber;
    }

    public String getDescription() {
        return description;
    }

    public int getStock() {
        return stock;
    }

    public int getMinStock() {
        return minStock;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setArticleNumber(String articleNumber) {
        this.articleNumber = articleNumber;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setMinStock(int minStock) {
        this.minStock = minStock;
    }
}