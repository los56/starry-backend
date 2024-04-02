package team.ubox.starry.dto.community;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class ResponseReplyDTO {
    private Integer postIndex;
    private String writer;
    private String content;
    private Timestamp writeDate;
}
