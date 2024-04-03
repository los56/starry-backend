package team.ubox.starry.dto.community;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import team.ubox.starry.entity.CommunityPost;
import team.ubox.starry.util.UUIDUtil;

import java.sql.Timestamp;
import java.util.List;

public class PostDTO {
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Response {
        private String writer;
        private String title;
        private String content;
        private Timestamp writeDate;
        private Timestamp modifyDate;
        private Integer repliesCount;

        private ResponseCommunityUserDTO writerData;

        public static Response from(CommunityPost entity) {
            Response instance = new Response();

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

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RequestWrite {
        @NotBlank
        @Length(max = 256)
        private String title;

        @NotBlank
        private String content;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RequestEdit {
        @NotNull
        private Integer index;

        @NotBlank
        @Length(max = 256)
        private String title;

        @NotBlank
        private String content;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RequestList {
        private String id;
        private Integer page;
        private Integer postPerPage;
        private Boolean isAscending = false;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ResponseList {
        private Integer page;
        private Integer allPageCount;
        private Long allPostsCount;
        private List<Response> posts;
    }
}
