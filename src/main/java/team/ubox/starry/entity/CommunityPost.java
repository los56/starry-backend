package team.ubox.starry.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity(name = "community_posts")
public class CommunityPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer index;

    @ManyToOne
    @JoinColumn(name = "writer")
    private User writer;

    private String title;
    private String content;
    private Timestamp writeDate = Timestamp.from(Instant.now());
    private Timestamp modifyDate;

    @Builder.Default
    @OneToMany(mappedBy = "index")
    private List<CommunityReply> replyList = new ArrayList<>();

    public void update(String title, String content) {
        this.title = title;
        this.content = content;

        this.modifyDate = Timestamp.from(Instant.now());
    }
}
