package ra.md04_project_part4.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.md04_project_part4.exception.CategoryException;
import ra.md04_project_part4.exception.ProductException;
import ra.md04_project_part4.model.dto.mapper.HttpResponse;
import ra.md04_project_part4.model.dto.mapper.ResponseMapper;
import ra.md04_project_part4.model.dto.response.CategoryResponseDTO;
import ra.md04_project_part4.model.dto.response.ProductResponseDTO;
import ra.md04_project_part4.service.category.ICategoryService;
import ra.md04_project_part4.service.product.IProductService;

import java.util.List;

@RestController
@RequestMapping("/api.myservice.com/v1")
public class HomeController {
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private IProductService productService;

    @GetMapping("/categories")
    // ResponseEntity<?>  đẩy dữ liệu vào body của Response
    public ResponseEntity<?> getCategory(@RequestParam(name = "keyword", required = false) String keyword,
                                         @RequestParam(defaultValue = "5", name = "limit") int limit,
                                         @RequestParam(defaultValue = "0", name = "page") int page,
                                         @RequestParam(defaultValue = "id", name = "sort") String sort,
                                         @RequestParam(defaultValue = "asc", name = "order") String order
    ) throws CategoryException {
        Page<CategoryResponseDTO> categoryPage = categoryService.getCategory(keyword, page, limit, sort, order);
        return new ResponseEntity<>( new ResponseMapper<>(
                HttpResponse.SUCCESS,
                HttpStatus.CREATED.value(),
                HttpStatus.CREATED.name(),
                categoryPage
        ), HttpStatus.OK);
    }

    // Danh sach product theo category
    @GetMapping("/products/categories/{id}")
    public ResponseEntity<?> getProductByCategoryId(@PathVariable Long id) throws ProductException {
        List<ProductResponseDTO> list = productService.getProductByCategoryId(id);
        return new ResponseEntity<>(
                new ResponseMapper<>(
                        HttpResponse.SUCCESS,
                        HttpStatus.CREATED.value(),
                        HttpStatus.CREATED.name(),
                        list
                ), HttpStatus.OK);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) throws ProductException {
        ProductResponseDTO productResponseDTO = productService.findById(id);
        return new ResponseEntity<>(new ResponseMapper<>(
                HttpResponse.SUCCESS,
                HttpStatus.CREATED.value(),
                HttpStatus.CREATED.name(),
                productResponseDTO
        ), HttpStatus.OK
        );
    }

    @GetMapping("/products")
    public ResponseEntity<?> getProducts(@RequestParam(name = "keyword", required = false) String keyword,
                                         @RequestParam(defaultValue = "0", name = "page") int page,
                                         @RequestParam(defaultValue = "5", name = "limit") int limit,
                                         @RequestParam(defaultValue = "id", name = "sort") String sort,
                                         @RequestParam(defaultValue = "asc", name = "order") String order) throws ProductException {
        Page<ProductResponseDTO> productPage = productService.getProduct(keyword, page, limit, sort, order);
        return new ResponseEntity<>(new ResponseMapper<>(
                HttpResponse.SUCCESS,
                HttpStatus.CREATED.value(),
                HttpStatus.CREATED.name(),
                productPage
        ), HttpStatus.OK);
    }

    @GetMapping("/products/new-products")
    public ResponseEntity<?> getNewProducts(@RequestParam(defaultValue = "5", name = "limit") int limit) throws ProductException {
        List<ProductResponseDTO> list = productService.findByOrderByCreatedAtDesc(limit);
        return new ResponseEntity<>(new ResponseMapper<>(
                HttpResponse.SUCCESS,
                HttpStatus.CREATED.value(),
                HttpStatus.CREATED.name(),
                list
        ), HttpStatus.OK);
    }


//    @GetMapping("/user/list")
//    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasAnyRole('USER','MANAGER')")
//    public String user(){
//        return "success";
//    }
}
