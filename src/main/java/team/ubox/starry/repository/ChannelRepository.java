package team.ubox.starry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.ubox.starry.entity.Channel;

import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository extends JpaRepository<Channel, UUID> {
    Optional<Channel> findByStreamKey(String streamKey);
}
