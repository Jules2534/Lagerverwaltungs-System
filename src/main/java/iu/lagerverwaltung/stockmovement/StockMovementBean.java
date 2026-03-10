package iu.lagerverwaltung.stockmovement;

import iu.lagerverwaltung.dto.ArticleDTO;
import iu.lagerverwaltung.dto.StockMovementDTO;
import iu.lagerverwaltung.service.ArticleService;
import iu.lagerverwaltung.service.StockMovementService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;

@Named
@RequestScoped
public class StockMovementBean {

    @Inject
    private StockMovementService stockMovementService;

    @Inject
    private ArticleService articleService;

    private StockMovementDTO newMovement = new StockMovementDTO();

    public StockMovementDTO getNewMovement() {
        return newMovement;
    }

    public void setNewMovement(StockMovementDTO newMovement) {
        this.newMovement = newMovement;
    }

    public List<StockMovementDTO> getMovements() {
        return stockMovementService.findAll();
    }

    public List<ArticleDTO> getArticles() {
        return articleService.findAll();
    }

    public MovementType[] getMovementTypes() {
        return MovementType.values();
    }

    public String saveMovement() {
        stockMovementService.createMovement(newMovement);
        return "stockmovements?faces-redirect=true";
    }

    public List<StockMovementDTO> getLowStockMovements() {
        return stockMovementService.findLowStockMovements();
    }
}