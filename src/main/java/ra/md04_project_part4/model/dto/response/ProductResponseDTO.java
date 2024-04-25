package ra.md04_project_part4.model.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ra.md04_project_part4.model.entity.Product;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductResponseDTO {
    private Long id;
    private String sku;
    private String productName;
    private String description;
    private double unitPrice;
    private String image;
    private String category;
    private boolean status;
    private int stock;

    public ProductResponseDTO(Product product) {
        this.id = product.getId();
        this.sku = product.getSku();
        this.productName = product.getProductName();
        this.description = product.getDescription();
        this.unitPrice = product.getUnitPrice();
        this.image = product.getImage();
        this.category = product.getCategory().getCategoryName();
        this.status = product.isStatus();
        this.stock = product.getStock();
    }

}
