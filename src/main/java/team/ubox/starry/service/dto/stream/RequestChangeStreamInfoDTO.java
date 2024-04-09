package team.ubox.starry.service.dto.stream;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
public class RequestChangeStreamInfoDTO {
    @NotBlank
    @Length(max = 100)
    private String streamTitle;

    @NotBlank
    private String streamCategory;
}
