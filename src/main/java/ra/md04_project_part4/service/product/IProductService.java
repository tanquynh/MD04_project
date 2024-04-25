package ra.md04_project_part4.service.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.md04_project_part4.exception.CategoryException;
import ra.md04_project_part4.exception.ProductException;
import ra.md04_project_part4.model.dto.request.ProductRequestDTO;
import ra.md04_project_part4.model.dto.response.ProductResponseDTO;
import ra.md04_project_part4.model.entity.Product;

import java.util.List;

public interface IProductService {
    Page<ProductResponseDTO> getProduct(String keyword, int page, int limit, String sort, String order ) throws ProductException;
    Page<ProductResponseDTO> findAllWithPaginationAndSort(Pageable pageable);
    List<ProductResponseDTO> findByOrderByCreatedAtDesc( int limit) throws ProductException;
    Page<ProductResponseDTO> searchByNameWithPaginationAndSort(Pageable pageable, String name);

    ProductResponseDTO findById(Long id) throws ProductException;

    ProductResponseDTO saveOrUpdate(ProductRequestDTO productRequestDTO) throws ProductException, CategoryException;

    void changeStatus(Long id) throws ProductException;

    ProductResponseDTO save(ProductRequestDTO productRequestDTO) throws ProductException, CategoryException;

    void increaseStock(Long productId);

    void decreaseStock(Long productId) throws ProductException;

    List<ProductResponseDTO> getProductByCategoryId(Long categoryId) throws ProductException;

    List<ProductResponseDTO> findAllByStatus(boolean status);


}
