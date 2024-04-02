package team.ubox.starry.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import team.ubox.starry.entity.CommunityPost;
import team.ubox.starry.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommunityPostRepository extends JpaRepository<CommunityPost, Integer> {
    Page<CommunityPost> findSliceByWriterId(UUID writerId, Pageable pageable);

    Integer countByWriter(User writer);

    Optional<CommunityPost> deleteByIndexAndWriterId(Integer index, UUID writerId);
}
