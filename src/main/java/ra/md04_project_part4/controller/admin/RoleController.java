package ra.md04_project_part4.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ra.md04_project_part4.exception.CustomException;
import ra.md04_project_part4.model.dto.mapper.HttpResponse;
import ra.md04_project_part4.model.dto.mapper.ResponseMapper;
import ra.md04_project_part4.model.entity.RoleName;
import ra.md04_project_part4.service.role.IRoleService;
import java.util.List;

@RestController
@RequestMapping("/api.myservice.com/v1/admin/roles")
public class RoleController {
    @Autowired
    private IRoleService roleService;

    @GetMapping("")
    public ResponseEntity<?> getRole() throws CustomException {
        List<RoleName> list = roleService.findAll();
        return new ResponseEntity<>(new ResponseMapper<>(
                HttpResponse.SUCCESS,
                HttpStatus.CREATED.value(),
                HttpStatus.CREATED.name(),
                list), HttpStatus.OK
        );
    }

}
