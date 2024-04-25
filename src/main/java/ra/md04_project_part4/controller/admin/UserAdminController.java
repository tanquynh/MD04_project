package ra.md04_project_part4.controller.admin;

import com.google.rpc.context.AttributeContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ra.md04_project_part4.exception.CustomException;
import ra.md04_project_part4.exception.UserException;
import ra.md04_project_part4.model.dto.mapper.HttpResponse;
import ra.md04_project_part4.model.dto.mapper.ResponseMapper;
import ra.md04_project_part4.model.dto.response.userResponse.UserResponseDTO;
import ra.md04_project_part4.repository.IUserRepository;
import ra.md04_project_part4.security.principle.UserDetailsCustom;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import ra.md04_project_part4.service.user.IUserService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api.myservice.com/v1/admin/users")
public class UserAdminController {
    @Autowired
    private IUserService userService;

    @GetMapping("")
    public ResponseEntity<?> getUsers(@RequestParam(name = "keyword", required = false) String keyword,
                                      @RequestParam(defaultValue = "0", name = "page") int page,
                                      @RequestParam(defaultValue = "5", name = "limit") int limit,
                                      @RequestParam(defaultValue = "id", name = "sort") String sort,
                                      @RequestParam(defaultValue = "asc", name = "order") String order) throws UserException {
        Page<UserResponseDTO> userPage = userService.getUser(keyword, page, sort, limit, order);
        return new ResponseEntity<>(
                new ResponseMapper<>(
                        HttpResponse.SUCCESS,
                        HttpStatus.CREATED.value(),
                        HttpStatus.CREATED.name(),
                        userPage
                )
                , HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) throws UserException {
        UserResponseDTO userResponseDTO = userService.findById(id);
        return new ResponseEntity<>(
                new ResponseMapper<>(
                        HttpResponse.SUCCESS,
                        HttpStatus.CREATED.value(),
                        HttpStatus.CREATED.name(),
                        userResponseDTO
                )
                , HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable Long id) throws UserException {
        UserResponseDTO userResponseDTO = userService.changeStatus(id);
        Map<String, UserResponseDTO> responseDTOMap = new HashMap<>();
        responseDTOMap.put("Change user status with id " + id + " successfully", userResponseDTO);
        return new ResponseEntity<>(responseDTOMap, HttpStatus.OK);
    }


    @PostMapping("/{id}/role/{role}")
    public ResponseEntity<?> addRole(@PathVariable Long id, @PathVariable Long role) throws UserException, CustomException {
        UserResponseDTO userResponseDTO = userService.addRole(role, id);
        Map<String, UserResponseDTO> responseDTOMap = new HashMap<>();
        responseDTOMap.put("Add user's role with id " + id + "successfully", userResponseDTO);
        return new ResponseEntity<>(
                new ResponseMapper<>(
                        HttpResponse.SUCCESS,
                        HttpStatus.CREATED.value(),
                        HttpStatus.CREATED.name(),
                        userResponseDTO
                ), HttpStatus.OK);
    }

    @DeleteMapping("/{id}/role/{roleId}")
    public ResponseEntity<?> deleteRole(@PathVariable Long id, @PathVariable Long roleId) throws UserException, CustomException {
        UserResponseDTO userResponseDTO = userService.delete(roleId, id);
        Map<String, UserResponseDTO> responseDTOMap = new HashMap<>();
        responseDTOMap.put("Add user's role with id " + id + "successfully", userResponseDTO);
        return new ResponseEntity<>(
                new ResponseMapper<>(
                        HttpResponse.SUCCESS,
                        HttpStatus.CREATED.value(),
                        HttpStatus.CREATED.name(),
                        userResponseDTO
                ), HttpStatus.OK);
    }

}
