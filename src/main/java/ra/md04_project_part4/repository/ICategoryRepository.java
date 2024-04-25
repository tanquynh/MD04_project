package ra.md04_project_part4.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ra.md04_project_part4.model.entity.Category;

import java.util.List;
@Transactional
public interface ICategoryRepository extends JpaRepository<Category, Long> {
    Page<Category> findAllByCategoryNameContainingIgnoreCase(Pageable pageable, String name);

    boolean existsByCategoryName(String name);

    @Modifying
    @Query("update Category c set c.status=case when c.status=true then false else true end where c.id=?1")
    void changeStatus(Long id);
    @Modifying
    @Query("update Category c set c.status= false where c.id=?1")
    void deleteCategory(Long id);
    Category findCategoryByCategoryName(String name);
    List<Category> findAllByStatus(boolean status);
}
