package ra.md04_project_part4.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ra.md04_project_part4.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

//    @Modifying
//    @Query("update User u set u.status = case  when u.status = true then false else true end where u.id=?1")
//    void changeStatus(Long id);
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.status = CASE WHEN u.status = true THEN false ELSE true END WHERE u.id = :userId")
    void changeStatus(@Param("userId") Long userId);

    @Modifying
    @Query("update User u set u.status = false where u.id = ?1")
    void blockUser(Long id);

    Page<User> findAllByUsernameContainingIgnoreCase(Pageable pageable, String username);
}
