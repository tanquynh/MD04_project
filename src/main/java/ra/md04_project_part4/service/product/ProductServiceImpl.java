package ra.md04_project_part4.service.product;

import org.apache.commons.lang3.RandomStringUtils;
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
import ra.md04_project_part4.model.dto.request.ProductRequestDTO;
import ra.md04_project_part4.model.dto.response.CategoryResponseDTO;
import ra.md04_project_part4.model.dto.response.ProductResponseDTO;
import ra.md04_project_part4.model.entity.Category;
import ra.md04_project_part4.model.entity.Product;
import ra.md04_project_part4.repository.IProductRepository;
import ra.md04_project_part4.service.UploadService;
import ra.md04_project_part4.service.category.ICategoryService;

import java.util.List;

@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private UploadService uploadService;
    @Autowired
    private ICategoryService categoryService;


    @Override
    public Page<ProductResponseDTO> getProduct(String keyword, int page, int limit, String sort, String order) throws ProductException {
        Sort.Direction direction = order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, limit, Sort.by(direction, sort));
        Page<ProductResponseDTO> productPage;
        if (keyword != null) {
            productPage = searchByNameWithPaginationAndSort(pageable, keyword);
        } else {
            productPage = findAllWithPaginationAndSort(pageable);

        }
        if (productPage == null || productPage.isEmpty()) {
            throw new ProductException("File image is not found", HttpStatus.NOT_FOUND);
        }
        return productPage;

    }

    @Override
    public Page<ProductResponseDTO> findAllWithPaginationAndSort(Pageable pageable) {
        Page<Product> list = productRepository.findAll(pageable);
        return list.map(ProductResponseDTO::new);
    }

    @Override
    public List<ProductResponseDTO> findByOrderByCreatedAtDesc(int limit) throws ProductException {
        List<Product> list = productRepository.findByOrderByCreatedAtDesc().stream().limit(limit).toList();
        if (list.isEmpty()) {
            throw new ProductException("Don't have any product", HttpStatus.BAD_REQUEST);
        }
        return list.stream().map(ProductResponseDTO::new).toList();
    }

    @Override
    public Page<ProductResponseDTO> searchByNameWithPaginationAndSort(Pageable pageable, String name) {
        Page<Product> list = productRepository.findProductByProductName(pageable, name);

        return list.map(ProductResponseDTO::new);
    }

    @Override
    public ProductResponseDTO findById(Long id) throws ProductException {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductException("Product is not found with this id " + id, HttpStatus.NOT_FOUND));

        return new ProductResponseDTO(product);
    }

    @Override
    public ProductResponseDTO saveOrUpdate(ProductRequestDTO productRequestDTO) throws ProductException, CategoryException {
        String fileName;
        if (productRequestDTO.getId() == null) {
            // check trùng productName
            if (productRepository.existsByProductName(productRequestDTO.getProductName())) {
                // lỗi xung đột
                throw new ProductException("Product's name exist", HttpStatus.CONFLICT);
            }
            if (productRequestDTO.getFile() == null || productRequestDTO.getFile().getSize() == 0) {
                throw new ProductException("File image is not found", HttpStatus.NOT_FOUND);
            }
            fileName = uploadService.uploadFileToServer(productRequestDTO.getFile());

        } else {
            ProductResponseDTO productResponseDTO = findById(productRequestDTO.getId());
            boolean productNameExist = productRepository.findAll().stream().anyMatch(product -> !productRequestDTO.getProductName().equalsIgnoreCase(productResponseDTO.getProductName())
                    && productRequestDTO.getProductName().equalsIgnoreCase(product.getProductName())
            );
            if (productNameExist) {
                throw new ProductException("Product's name existed", HttpStatus.CONFLICT);
            }
            //uploadfile
            //kiem tra xem co edit file anh khong ?
            if (productRequestDTO.getFile() != null && productRequestDTO.getFile().getSize() > 0) {
                fileName = uploadService.uploadFileToServer(productRequestDTO.getFile());
            } else {
                fileName = productResponseDTO.getImage();
            }
        }
        // Check Category tồn tại
        CategoryResponseDTO categoryResponseDTO = categoryService.findById(productRequestDTO.getCategoryId());
        Category category = new Category();
        if (categoryResponseDTO != null) {
            category = Category.builder()
                    .id(categoryResponseDTO.getId())
                    .categoryName(categoryResponseDTO.getCategoryName())
                    .status(categoryResponseDTO.isStatus())
                    .products(categoryResponseDTO.getProducts())
                    .build();
        }
        Product product = productRepository.save(Product.builder()
                .id(productRequestDTO.getId())
                .sku(RandomStringUtils.randomAlphanumeric(8))
                .stock(productRequestDTO.getStock())
                .status(productRequestDTO.isStatus())
                .image(fileName)
                .unitPrice(productRequestDTO.getUnitPrice())
                .category(category)
                .createdAt(productRequestDTO.getCreatedAt())
                .build()
        );

        return new ProductResponseDTO(product);
    }

    @Override
    public void changeStatus(Long id) throws ProductException {
        ProductResponseDTO productResponseDTO = findById(id);
        if (productResponseDTO != null) {
            productRepository.changeStatus(id);
        }
    }

    @Override
    public ProductResponseDTO save(ProductRequestDTO productRequestDTO) throws ProductException, CategoryException {
        if (productRepository.existsByProductName(productRequestDTO.getProductName())) {
            throw new ProductException("Product's name existed", HttpStatus.BAD_REQUEST);
        }
        // check ProductName tồn tại
        CategoryResponseDTO categoryResponseDTO = categoryService.findById(productRequestDTO.getCategoryId());
        Category category = new Category();
        if (categoryResponseDTO != null) {
            category = Category.builder()
                    .id(categoryResponseDTO.getId())
                    .categoryName(categoryResponseDTO.getCategoryName())
                    .status(categoryResponseDTO.isStatus())
                    .products(categoryResponseDTO.getProducts())
                    .build();
        }
        // upload file
        if (productRequestDTO.getFile() == null || productRequestDTO.getFile().getSize() == 0) {
            throw new ProductException("File image is not found", HttpStatus.BAD_REQUEST);
        }
        String fileName = uploadService.uploadFileToServer(productRequestDTO.getFile());
        Product product = productRepository.save(Product.builder()
                .productName(productRequestDTO.getProductName())
                .description(productRequestDTO.getDescription())
                .stock(productRequestDTO.getStock())
                .status(productRequestDTO.isStatus())
                .image(fileName)
                .unitPrice(productRequestDTO.getUnitPrice())
                .category(category)
                .createdAt(productRequestDTO.getCreatedAt())
                .build());
        return new ProductResponseDTO(product);
    }

    @Override
    public void increaseStock(Long productId) {
        productRepository.increaseStock(productId);
    }

    @Override
    public void decreaseStock(Long productId) throws ProductException {
        ProductResponseDTO productResponseDTO = findById(productId);
        if (productResponseDTO.getStock() > 0) {
            productRepository.decreaseStock(productId);
        }
        if (productResponseDTO.getStock() == 0) {
            productRepository.setStatusFalse(productId);
        }
    }

    @Override
    public List<ProductResponseDTO> getProductByCategoryId(Long categoryId) throws ProductException {
        List<Product> list = productRepository.findAllByCategory_Id(categoryId);
        if (list.isEmpty()) {
            throw new ProductException("Don't have any product belonging category with id " + categoryId, HttpStatus.BAD_REQUEST);
        }
        return list.stream().map(ProductResponseDTO::new).toList();
    }

    @Override
    public List<ProductResponseDTO> findAllByStatus(boolean status) {
        List<Product> list = productRepository.findAllByStatus(status);
        return list.stream().map(ProductResponseDTO::new).toList();
    }
}
