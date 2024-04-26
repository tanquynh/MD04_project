package ra.md04_project_part4.service.role;

import ra.md04_project_part4.exception.CustomException;
import ra.md04_project_part4.model.entity.Role;
import ra.md04_project_part4.model.entity.RoleName;

import java.util.List;

public interface IRoleService {
    Role findRoleByRoleId(Long roleId) throws CustomException;
    List<RoleName> findAll() throws CustomException;

}
