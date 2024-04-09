package team.ubox.starry.repository.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity(name = "static_files")
public class StaticFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer index;

    private UUID uploader;
    private String fileName;
    private String originalFileName;
    private Timestamp uploadDate;
    private String uploaderIp;
}
