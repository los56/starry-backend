package team.ubox.starry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.ubox.starry.entity.CommunityPost;
import team.ubox.starry.entity.CommunityReply;
import team.ubox.starry.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommunityReplyRepository extends JpaRepository<CommunityReply, Integer> {
    List<CommunityReply> findByWriter(User writer);
    List<CommunityReply> findByPost(CommunityPost post);

    Optional<CommunityReply> deleteByIndexAndWriterId(Integer index, UUID writerID);
}
