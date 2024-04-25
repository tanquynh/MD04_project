package ra.md04_project_part4.service.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import ra.md04_project_part4.exception.CustomException;
import ra.md04_project_part4.exception.UserException;
import ra.md04_project_part4.model.dto.request.FormLogin;
import ra.md04_project_part4.model.dto.request.FormRegister;
import ra.md04_project_part4.model.dto.response.CategoryResponseDTO;
import ra.md04_project_part4.model.dto.response.JWTResponse;
import ra.md04_project_part4.model.dto.response.userResponse.UserResponseDTO;
import ra.md04_project_part4.model.entity.Category;
import ra.md04_project_part4.model.entity.Role;
import ra.md04_project_part4.model.entity.RoleName;
import ra.md04_project_part4.model.entity.User;
import ra.md04_project_part4.repository.IRoleRepository;
import ra.md04_project_part4.repository.IUserRepository;
import ra.md04_project_part4.security.jwt.JWTProvider;
import ra.md04_project_part4.security.principle.UserDetailsCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ra.md04_project_part4.service.role.IRoleService;

import java.time.LocalDate;
import java.util.*;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private IRoleRepository iRoleRepository;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTProvider jwtProvider;
    @Autowired
    private IUserRepository userRepository;

    @Override
    public boolean register(FormRegister formRegister) {
        User user = User.builder()
                .email(formRegister.getEmail())
                .fullName(formRegister.getFullName())
                .username(formRegister.getUsername())
                .createdAt(LocalDate.now())
                .password(passwordEncoder.encode(formRegister.getPassword()))
                .status(true)
                .build();
        if (formRegister.getRoles() != null && !formRegister.getRoles().isEmpty()) {
            Set<Role> roles = new HashSet<>();
            formRegister.getRoles().forEach(
                    r -> {
                        switch (r) {
                            case "ADMIN":
                                roles.add(iRoleRepository.findByRoleName(RoleName.ROLE_ADMIN).orElseThrow(() -> new NoSuchElementException("role not found")));
                            case "MANAGER":
                                roles.add(iRoleRepository.findByRoleName(RoleName.ROLE_MANAGER).orElseThrow(() -> new NoSuchElementException("role not found")));
                            case "USER":
                                roles.add(iRoleRepository.findByRoleName(RoleName.ROLE_USER).orElseThrow(() -> new NoSuchElementException("role not found")));
                            default:
                                roles.add(iRoleRepository.findByRoleName(RoleName.ROLE_USER).orElseThrow(() -> new NoSuchElementException("role not found")));
                        }
                    }
            );
            user.setRoleSet(roles);
        } else {
            // mac dinh la user
            Set<Role> roles = new HashSet<>();
            roles.add(iRoleRepository.findByRoleName(RoleName.ROLE_USER).orElseThrow(() -> new NoSuchElementException("role not found")));
            user.setRoleSet(roles);
        }
        userRepository.save(user);
        return true;
    }

    @Override
    public JWTResponse login(FormLogin formLogin) {
        // xac thực username vaf password
        Authentication authentication = null;
        try {
            authentication = manager.authenticate(new UsernamePasswordAuthenticationToken(formLogin.getUsername(), formLogin.getPassword()));
        } catch (AuthenticationException e) {
            throw new RuntimeException("username or password incorrect");
        }
        UserDetailsCustom detailsCustom = (UserDetailsCustom) authentication.getPrincipal();
        String accessToken = jwtProvider.generateAccessToken(detailsCustom);
        return JWTResponse.builder()
                .email(detailsCustom.getEmail())
                .fullName(detailsCustom.getFullName())
                .roleSet(detailsCustom.getAuthorities())
                .status(detailsCustom.isStatus())
                .accessToken(accessToken)
                .build();
    }


    @Override
    public Page<UserResponseDTO> getUser(String keyword, int page, String sort, int limit, String order) throws UserException {
        Sort.Direction direction = order.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, limit, Sort.by(direction, sort));
        Page<UserResponseDTO> userResponseDTOS;
        if (keyword != null && keyword.isEmpty()) {
            userResponseDTOS = searchByNameWithPaginationAndSort(pageable, keyword);
        } else {
            userResponseDTOS = findAllWithPaginationAndSort(pageable);
        }
        if (userResponseDTOS == null || userResponseDTOS.isEmpty()) {
            throw new UserException("Don't have any users.", HttpStatus.NOT_FOUND);
        }
        return userResponseDTOS;
    }

    @Override
    public UserResponseDTO findById(Long id) throws UserException {
        User user = userRepository.findById(id).orElseThrow(() ->
                new UserException("User is not found with this id " + id, HttpStatus.NOT_FOUND));
        return new UserResponseDTO(user);
    }

    @Override
    public UserResponseDTO addRole(Long role, Long id) throws UserException, CustomException {
        UserResponseDTO userResponseDTO = findById(id);
        if (userResponseDTO.getRoles().stream().anyMatch(role1 -> Objects.equals(role1.getId(), id))) {
            throw new UserException("User's role already exists", HttpStatus.CONFLICT);
        }
        userResponseDTO.getRoles().add(roleService.findRoleByRoleId(role));

        return updateUser(id);
    }

    @Override
    public UserResponseDTO updateUser(Long id) throws UserException {
        User user = userRepository.findById(id).orElseThrow(() ->
                new UserException("User is not found with this id " + id, HttpStatus.NOT_FOUND));
        return new UserResponseDTO(userRepository.save(user));
    }

    @Override
    public UserResponseDTO changeStatus(Long id) throws UserException {
        // Tìm người dùng dựa trên id
        UserResponseDTO userResponseDTO = findById(id);

        // Kiểm tra xem người dùng có tồn tại không
        if (userResponseDTO == null) {
            throw new UserException("User not found", HttpStatus.NOT_FOUND);
        }

        // Kiểm tra xem người dùng có vai trò ADMIN không
        boolean isAdmin = userResponseDTO.getRoles().stream()
                .anyMatch(role -> role.getRoleName() == RoleName.ROLE_ADMIN);
        if (isAdmin) {
            throw new UserException("Can't change ADMIN's status", HttpStatus.BAD_REQUEST);
        }

        // Cập nhật trạng thái của người dùng
        userRepository.changeStatus(id);

        // Trả về thông tin người dùng sau khi thay đổi trạng thái
        return userResponseDTO;
    }

    @Override
    public UserResponseDTO delete(Long roleId, Long id) throws UserException, CustomException {
        UserResponseDTO user = findById(id);
        if (user.getId() == 502) {
            throw new UserException("User's role has not been existed", HttpStatus.BAD_REQUEST);

        }
        user.getRoles().remove(roleService.findRoleByRoleId(roleId));
        return updateUser(id);
    }

    private Page<UserResponseDTO> findAllWithPaginationAndSort(Pageable pageable) {
        Page<User> list = userRepository.findAll(pageable);
        return list.map(UserResponseDTO::new);
    }

    private Page<UserResponseDTO> searchByNameWithPaginationAndSort(Pageable pageable, String name) {
        Page<User> list = userRepository.findAllByUsernameContainingIgnoreCase(pageable, name);
        return list.map(UserResponseDTO::new);
    }

}


