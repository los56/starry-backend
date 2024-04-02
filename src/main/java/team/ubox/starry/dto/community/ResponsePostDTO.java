package team.ubox.starry.dto.community;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team.ubox.starry.entity.CommunityPost;
import team.ubox.starry.util.UUIDUtil;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class ResponsePostDTO {
    private String writer;
    private String title;
    private String content;
    private Timestamp writeDate;
    private Timestamp modifyDate;
    private Integer repliesCount;

    public static ResponsePostDTO from(CommunityPost entity) {
        ResponsePostDTO instance = new ResponsePostDTO();

        String idStr = UUIDUtil.UUIDToString(entity.getWriter().getId());
        instance.setWriter(idStr);
        instance.setTitle(entity.getTitle());
        instance.setContent(entity.getContent());
        instance.setWriteDate(entity.getWriteDate());
        instance.setModifyDate(entity.getModifyDate());
        instance.setRepliesCount(entity.getReplyList().size());

        return instance;
    }
}
