package ra.md04_project_part4.service.user;

import org.springframework.data.domain.Page;
import ra.md04_project_part4.exception.CustomException;
import ra.md04_project_part4.exception.UserException;
import ra.md04_project_part4.model.dto.request.FormLogin;
import ra.md04_project_part4.model.dto.request.FormRegister;
import ra.md04_project_part4.model.dto.response.JWTResponse;
import ra.md04_project_part4.model.dto.response.userResponse.UserResponseDTO;

public interface IUserService {
    boolean register(FormRegister formRegister);

    JWTResponse login(FormLogin formLogin);

    Page<UserResponseDTO> getUser(String keyword, int page, String sort, int limit, String order) throws UserException;

    UserResponseDTO findById(Long id) throws UserException;

    UserResponseDTO addRole(Long role, Long id) throws UserException, CustomException;

    UserResponseDTO updateUser(Long id) throws UserException;
    UserResponseDTO changeStatus(Long id) throws UserException;
    UserResponseDTO delete(Long roleId, Long id) throws UserException, CustomException;
}
