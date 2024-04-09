package team.ubox.starry.repository.entity;

import jakarta.persistence.*;
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
@Entity(name = "streams")
public class Stream {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "streamer")
    private User streamer;

    @Column(name = "streamer", insertable = false, updatable = false)
    private UUID streamer_id;

    private String lastTitle;
    private String lastCategory;
    private Boolean opened;
    private Timestamp openTime;
    private Timestamp closeTime;
}
