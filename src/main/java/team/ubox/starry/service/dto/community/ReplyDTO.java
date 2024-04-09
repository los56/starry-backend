package team.ubox.starry.service.dto.community;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

public class ReplyDTO {
    @Getter
    @Setter
    @NoArgsConstructor
    public static class RequestWrite {
        @NotNull
        private Integer postIndex;

        @NotBlank
        private String content;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RequestList {
        @NotNull
        private Integer postIndex;

        private Integer page = 1;
        private Integer replyPerPage = 30;
    }


    @Getter
    @Setter
    @NoArgsConstructor
    public static class Response {
        private Integer postIndex;
        private String writer;
        private String content;
        private Timestamp writeDate;

        private ResponseCommunityUserDTO writerData;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ResponseList {
        private Integer page;
        private Integer allPageCount;
        private Integer allRepliesCount;
        private List<Response> replies;
    }
}
