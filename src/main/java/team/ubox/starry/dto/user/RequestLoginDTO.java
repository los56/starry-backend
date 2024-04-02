package team.ubox.starry.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
public class RequestLoginDTO {
    @NotBlank
    @Length(min = 4, max = 128)
    private String username;

    @NotBlank
    @Length(min = 8)
    private String password;
}
