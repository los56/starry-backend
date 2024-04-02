package team.ubox.starry.entity;

import com.zaxxer.hikari.util.UtilityElf;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity(name = "channels")
public class Channel {
    @Id
    private UUID id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id", referencedColumnName = "id")
    private User owner;

    private String description;
    private Boolean verified;
    private String bannerUrl;
    private String streamKey;
    private String streamTitle;
    private String streamCategory;
    private Timestamp lastOpenTime;
    private Timestamp lastCloseTime;
    private String lastStreamId;

    public void updateStreamKey(String key) {
        this.streamKey = key;
    }

    public void updateStreamInfo(String streamTitle, String streamCategory) {
        this.streamTitle = streamTitle;
        this.streamCategory = streamCategory;
    }

    public void updateLastStream(String streamId, Timestamp open, Timestamp close) {
        this.lastOpenTime = open;
        this.lastCloseTime = close;
        this.lastStreamId = streamId;
    }
}
