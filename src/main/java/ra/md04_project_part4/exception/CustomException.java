package ra.md04_project_part4.exception;

import org.springframework.http.HttpStatus;

public class CustomException extends Exception {
    private HttpStatus status;

    public CustomException( String message,HttpStatus status) {
       super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
