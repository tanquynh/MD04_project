package ra.md04_project_part4.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ra.md04_project_part4.exception.CategoryException;
import ra.md04_project_part4.exception.ProductException;
import ra.md04_project_part4.model.dto.response.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class APIControllerAdvice {
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String, Object> forbidden(AccessDeniedException e) {
        Map<String, Object> map = new HashMap<>();
        map.put("error", new ResponseError(403, "FOR_BIDDEN", e));
        return map;
    }

    @ExceptionHandler(ProductException.class)
    public Map<String, Object> productException(ProductException e) {
        Map<String, Object> map = new HashMap<>();
        map.put("error", new ResponseError(e.getHttpStatus().value(), e.getHttpStatus().name(), e.getMessage()));
        return map;
    }

    @ExceptionHandler(CategoryException.class)
    public Map<String, Object> categoryException(CategoryException e) {
        Map<String, Object> map = new HashMap<>();
        map.put("error", new ResponseError(e.getHttpStatus().value(), e.getHttpStatus().name(), e.getMessage()));
        return map;
    }


}
