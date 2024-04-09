package team.ubox.starry.repository.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Entity(name = "follows")
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer index;

    private UUID fromUser;
    private UUID toUser;
    private Timestamp followDate;

    public Follow(UUID from, UUID to) {
        this.fromUser = from;
        this.toUser = to;
        this.followDate = Timestamp.from(Instant.now());
    }
}
