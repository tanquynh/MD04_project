package ra.md04_project_part4.exception;

import org.springframework.http.HttpStatus;

public class ProductException extends Exception {
    private HttpStatus httpStatus;

    public ProductException(String message, HttpStatus status) {
        super(message);
        this.httpStatus = status;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
