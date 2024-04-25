package ra.md04_project_part4.model.dto.response.userResponse;

import lombok.*;
import ra.md04_project_part4.model.entity.Address;
import ra.md04_project_part4.model.entity.Role;
import ra.md04_project_part4.model.entity.User;

import java.util.Set;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserResponseDTO {
    private Long id;
    private String fullName;
    private String username;
    private String email;
    private boolean status;
    private Set<Address> address;
    private String phone;
    private Set<Role> roles;

    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.status = user.isStatus();
        this.address = user.getAddress();
        this.phone = user.getPhone();
        this.roles = user.getRoleSet();
    }
}
