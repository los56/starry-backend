package team.ubox.starry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.ubox.starry.repository.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    Optional<User> findByNickname(String nickname);
    Optional<User> findByEmail(String email);
}
