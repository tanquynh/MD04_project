package ra.md04_project_part4.service.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ra.md04_project_part4.exception.CategoryException;
import ra.md04_project_part4.exception.ProductException;
import ra.md04_project_part4.model.dto.request.CategoryRequestDTO;
import ra.md04_project_part4.model.dto.response.CategoryResponseDTO;
import ra.md04_project_part4.model.entity.Category;
import ra.md04_project_part4.repository.ICategoryRepository;

import java.util.List;

@Service
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    private ICategoryRepository categoryRepository;

    @Override
    public Page<CategoryResponseDTO> getCategory(String keyword, int page, int limit, String sort, String order) throws CategoryException {
        Sort.Direction direction = order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, limit, Sort.by(direction, sort));
        Page<CategoryResponseDTO> categoryPage;
        if (keyword != null && !keyword.isEmpty()) {
            categoryPage = searchByNameWithPaginationAndSort(pageable, keyword);
        } else {
            categoryPage = findAllWithPaginationAndSort(pageable);
        }
        if (categoryPage == null || categoryPage.isEmpty()) {
            throw new CategoryException("Category is not found", HttpStatus.NOT_FOUND);

        }
        return categoryPage;
    }

    @Override
    public Page<CategoryResponseDTO> findAllWithPaginationAndSort(Pageable pageable) {
        Page<Category> list = categoryRepository.findAll(pageable);
        return list.map(CategoryResponseDTO::new);
    }

    @Override
    public Page<CategoryResponseDTO> searchByNameWithPaginationAndSort(Pageable pageable, String name) {
        Page<Category> list = categoryRepository.findAllByCategoryNameContainingIgnoreCase(pageable, name);
        return list.map(CategoryResponseDTO::new);
    }

    @Override
    public CategoryResponseDTO findById(Long id) throws CategoryException {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new CategoryException("Category is not found!!" + id, HttpStatus.NOT_FOUND));
        return CategoryResponseDTO.builder()
                .id(category.getId())
                .categoryName(category.getCategoryName())
                .status(category.isStatus())
                .products(category.getProducts())
                .build()
                ;
    }

    @Override
    public CategoryResponseDTO saveOrUpdate(CategoryRequestDTO categoryRequestDTO) throws CategoryException {
        if (categoryRequestDTO.getId() == null) {
            if (categoryRepository.existsByCategoryName(categoryRequestDTO.getCategoryName())) {
                throw new CategoryException("Category name has been already existed!", HttpStatus.CONFLICT);
            }

        } else {
            CategoryResponseDTO editCategoryResponseDTO = findById(categoryRequestDTO.getId());
            boolean categoryExist = categoryRepository.findAll().stream().anyMatch
                    (category -> (!categoryRequestDTO.getCategoryName().equals(editCategoryResponseDTO.getCategoryName()) && categoryRequestDTO.getCategoryName().equals(category.getCategoryName())));

            if (categoryExist) {
                throw new CategoryException("Category name has been already existed", HttpStatus.CONFLICT);

            }
        }
        Category category = categoryRepository.save(Category.builder()
                .id(categoryRequestDTO.getId())
                .categoryName(categoryRequestDTO.getCategoryName())
                .description(categoryRequestDTO.getDescription())
                .status(categoryRequestDTO.isStatus())
                .build());
        return new CategoryResponseDTO(category);
    }

    @Override
    public void changeStatus(Long id) throws CategoryException {
        CategoryResponseDTO categoryResponseDTO = findById(id);
        if (categoryResponseDTO != null) {
            categoryRepository.changeStatus(id);
        }
    }

    @Override
    public List<CategoryResponseDTO> findAllByStatus(boolean status) {
        List<Category> categories = categoryRepository.findAllByStatus(status);
        return categories.stream().map(CategoryResponseDTO::new).toList();
    }

    @Override
    public List<CategoryResponseDTO> findAll() {
        List<Category> list = categoryRepository.findAll();
        return list.stream().map(CategoryResponseDTO::new).toList();
    }

    @Override
    public CategoryResponseDTO editCategory(Long id, CategoryRequestDTO categoryRequestDTO) throws CategoryException {
        categoryRequestDTO.setId(id);
        CategoryResponseDTO categoryResponseDTO = saveOrUpdate(categoryRequestDTO);
        return categoryResponseDTO;
    }


}
