package ra.md04_project_part4.model.dto.mapper;

import lombok.*;

import javax.xml.crypto.Data;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ResponseMapper<T> {
    private HttpResponse httpResponse;
    private int code;
    private String message;
    private T data;

}
