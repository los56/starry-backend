package team.ubox.starry.dto.community;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RequestReplyListDTO {
    @NotNull
    private Integer postIndex;

    private Integer page = 1;
    private Integer replyPerPage = 30;
}
