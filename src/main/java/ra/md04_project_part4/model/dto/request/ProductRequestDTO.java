package ra.md04_project_part4.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import ra.md04_project_part4.model.entity.Product;

import java.time.LocalDate;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder

public class ProductRequestDTO {
    private Long id;
    @NotEmpty(message = "ProductName is mandatory!")
    private String productName;

    @NotEmpty(message = "Description is mandatory!")
    private String description;

    @NotNull(message = "Stock is mandatory!")
    @PositiveOrZero(message = "Stock must be greater than or equal to 0")
    private int stock;
    private boolean status = true;
    private MultipartFile file;
    private LocalDate createdAt = LocalDate.now();

    @PositiveOrZero(message = "unitPrice must be greater than 0")
    private double unitPrice;

    @NotNull(message = "Category is mandatory!")
    private Long categoryId;

}
