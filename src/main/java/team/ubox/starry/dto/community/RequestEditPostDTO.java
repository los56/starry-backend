package team.ubox.starry.dto.community;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
public class RequestEditPostDTO {
    @NotNull
    private Integer index;

    @NotBlank
    @Length(max = 256)
    private String title;

    @NotBlank
    private String content;
}
