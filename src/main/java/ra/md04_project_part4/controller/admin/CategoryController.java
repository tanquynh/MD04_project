package ra.md04_project_part4.controller.admin;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.md04_project_part4.exception.CategoryException;
import ra.md04_project_part4.model.dto.mapper.HttpResponse;
import ra.md04_project_part4.model.dto.mapper.ResponseMapper;
import ra.md04_project_part4.model.dto.request.CategoryRequestDTO;
import ra.md04_project_part4.model.dto.request.FormShow;
import ra.md04_project_part4.model.dto.response.CategoryResponseDTO;
import ra.md04_project_part4.service.category.ICategoryService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api.myservice.com/v1/admin/categories")
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;

    @GetMapping("")
    public ResponseEntity<?> getCategory(@RequestParam(name = "keyword", required = false) String keyword,
                                         @RequestParam(defaultValue = "5", name = "limit") int limit,
                                         @RequestParam(defaultValue = "0", name = "page") int page,
                                         @RequestParam(defaultValue = "id", name = "sort") String sort,
                                         @RequestParam(defaultValue = "asc", name = "order") String order) throws CategoryException {
        Page<CategoryResponseDTO> categoryPage = categoryService.getCategory(keyword, page, limit, sort, order);
        return new ResponseEntity<>(new ResponseMapper<>(
                HttpResponse.SUCCESS,
                HttpStatus.CREATED.value(),
                HttpStatus.CREATED.name(),
                categoryPage
        ), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) throws CategoryException {
        CategoryResponseDTO categoryResponseDTO = categoryService.findById(id);
        return new ResponseEntity<>(new ResponseMapper<>(
                HttpResponse.SUCCESS,
                HttpStatus.CREATED.value(),
                HttpStatus.CREATED.name(),
                categoryResponseDTO
        ), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> addCategory(@RequestBody @Valid CategoryRequestDTO categoryRequestDTO) throws CategoryException {
        CategoryResponseDTO categoryResponseDTO = categoryService.saveOrUpdate(categoryRequestDTO);
        Map<String, CategoryResponseDTO> responseDTOMap = new HashMap<>();
        responseDTOMap.put("Add new genre successfully", categoryResponseDTO);
        return new ResponseEntity<>(
                new ResponseMapper<>(
                        HttpResponse.SUCCESS,
                        HttpStatus.CREATED.value(),
                        HttpStatus.CREATED.name(),
                        responseDTOMap), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editCategory(@PathVariable Long id, @RequestBody @Valid CategoryRequestDTO categoryRequestDTO) throws CategoryException {
        CategoryResponseDTO categoryResponseDTO = categoryService.editCategory(id, categoryRequestDTO);
        Map<String, CategoryResponseDTO> response = new HashMap<>();
        response.put("Edit category successfully", categoryResponseDTO);
        return new ResponseEntity<>(
                response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCate(@PathVariable Long id) throws CategoryException {
        CategoryResponseDTO deleteCate = categoryService.findById(id);
        categoryService.changeStatus(id);
        Map<String, CategoryResponseDTO> response = new HashMap<>();
        response.put("Edit Category successfully", deleteCate);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
