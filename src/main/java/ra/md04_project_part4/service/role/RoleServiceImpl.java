package ra.md04_project_part4.service.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ra.md04_project_part4.exception.CustomException;
import ra.md04_project_part4.model.entity.Role;
import ra.md04_project_part4.model.entity.RoleName;
import ra.md04_project_part4.repository.IRoleRepository;

import java.util.List;

@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired
    private IRoleRepository repository;

    @Override
    public Role findRoleByRoleId(Long id) throws CustomException {

        return repository.findAllById(id).orElseThrow(() -> new CustomException("Don't have any role", HttpStatus.BAD_REQUEST));
    }

    @Override
    public List<RoleName> findAll() throws CustomException {
        List<Role> list = repository.findAll();
        if (list.isEmpty()) {
            throw new CustomException("Don't have any role", HttpStatus.BAD_REQUEST);

        }
        return list.stream().map(Role::getRoleName).toList();
    }
}
