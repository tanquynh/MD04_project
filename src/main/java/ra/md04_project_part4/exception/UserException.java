package ra.md04_project_part4.exception;

import org.springframework.http.HttpStatus;

public class UserException extends Exception {
    private HttpStatus httpStatus;

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public UserException(String message, HttpStatus status) {
        super(message);
        this.httpStatus = status;

    }
}
