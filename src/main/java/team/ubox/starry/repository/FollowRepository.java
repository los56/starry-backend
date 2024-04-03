package team.ubox.starry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.ubox.starry.entity.Follow;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FollowRepository extends JpaRepository<Follow, Integer> {
    Integer countByToUser(UUID user);
    List<Follow> findAllByFromUser(UUID user);

    Optional<Follow> findByFromUserAndToUser(UUID fromUser, UUID toUser);
}
