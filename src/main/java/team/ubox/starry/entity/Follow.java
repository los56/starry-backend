package team.ubox.starry.entity;

import jakarta.persistence.*;
import lombok.*;

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

    public Follow(UUID from, UUID to) {
        this.fromUser = from;
        this.toUser = to;
    }
}
