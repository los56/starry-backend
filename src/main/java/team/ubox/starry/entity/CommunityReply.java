package team.ubox.starry.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity(name = "community_replies")
public class CommunityReply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer index;

    @ManyToOne
    @JoinColumn(name = "writer")
    private User writer;

    @ManyToOne
    @JoinColumn(name = "post")
    private CommunityPost post;

    private String content;

    private Timestamp writeDate = Timestamp.from(Instant.now());
}
