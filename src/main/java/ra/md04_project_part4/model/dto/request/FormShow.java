package ra.md04_project_part4.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class FormShow {
    private String keyword;
    private int limit;
    private int page;
    private String sort = "id";
    private String order = "asc";
}
