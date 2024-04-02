package team.ubox.starry.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseLoginDTO {
    private String accessToken;
    private String refreshToken;
}
