package team.ubox.starry.dto.stream;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RequestChangeStreamInfoDTO {
    @NotBlank
    private String streamTitle;

    @NotBlank
    private String streamCategory;
}
