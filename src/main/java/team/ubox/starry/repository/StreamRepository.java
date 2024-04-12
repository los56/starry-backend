package team.ubox.starry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.ubox.starry.repository.entity.Stream;

import java.util.UUID;

public interface StreamRepository extends JpaRepository<Stream, UUID> {
}
