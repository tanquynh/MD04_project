package ra.md04_project_part4.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ra.md04_project_part4.model.entity.Product;

import java.util.List;

@Transactional
public interface IProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findProductByProductName(Pageable pageable, String name);

    Page<Product> findAllByProductNameContainingIgnoreCase(Pageable pageable, String name);

    boolean existsByProductName(String name);

    @Modifying
    @Query("update Product p set p.status=case when p.status = true then false else true end where p.id=?1")
    void changeStatus(Long id);

    @Modifying
    @Query("UPDATE Product p SET p.status = CASE WHEN p.status = true THEN false ELSE true END WHERE p.id = ?1")
    void toggleProductStatus(Long id);

    @Modifying
    @Query("SELECT p FROM Product p ORDER BY p.createdAt DESC")
    List<Product> findByOrderByCreatedAtDesc();


    @Modifying
    @Query("update Product p set p.stock = p.stock+1 where p.id=?1")
    void increaseStock(Long productId);

    @Modifying
    @Query("update Product  p set p.stock = p.stock -1 where p.id=?1")
    void decreaseStock(Long productId);

    @Modifying
    @Query("update Product  p set p.status = false where p.id=?1")
    void setStatusFalse(Long id);

    List<Product> findAllByCategory_Id(Long categoryId);

    List<Product> findAllByStatus(boolean status);

    Product findProductByProductName(String productName);
}
