package team.ubox.starry.dto.community;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RequestWriteReplyDTO {
    @NotNull
    private Integer postIndex;

    @NotBlank
    private String content;
}
