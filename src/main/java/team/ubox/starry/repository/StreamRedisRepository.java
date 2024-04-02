package team.ubox.starry.repository;

import org.springframework.data.repository.CrudRepository;
import team.ubox.starry.entity.Stream;

import java.util.UUID;

public interface StreamRedisRepository extends CrudRepository<Stream, UUID> {
}
