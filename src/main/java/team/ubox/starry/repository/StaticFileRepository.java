package team.ubox.starry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.ubox.starry.repository.entity.StaticFile;

import java.util.Optional;

public interface StaticFileRepository extends JpaRepository<StaticFile, Integer> {
    Optional<StaticFile> findByFileName(String fileName);
}
