package ra.md04_project_part4.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.md04_project_part4.exception.ProductException;
import ra.md04_project_part4.model.dto.mapper.HttpResponse;
import ra.md04_project_part4.model.dto.mapper.ResponseMapper;
import ra.md04_project_part4.model.dto.response.ProductResponseDTO;
import ra.md04_project_part4.service.product.IProductService;

@RestController
@RequestMapping("/api.myservice.com/v1/admin/products")
public class ProductController {
    @Autowired
    private IProductService productService;

    @GetMapping("")
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

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductDetail(@PathVariable Long id) throws ProductException {
        ProductResponseDTO productResponseDTO = productService.findById(id);
        return new ResponseEntity<>(new ResponseMapper<>(
                HttpResponse.SUCCESS,
                HttpStatus.CREATED.value(),
                HttpStatus.CREATED.name(),
                productResponseDTO
        ), HttpStatus.OK);
    }
}
