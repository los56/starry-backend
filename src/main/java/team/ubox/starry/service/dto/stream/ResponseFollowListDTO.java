package team.ubox.starry.service.dto.stream;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ResponseFollowListDTO {
    private List<ResponseStreamDTO> list;

    public ResponseFollowListDTO(List<ResponseStreamDTO> list) {
        this.list = list;
    }
}
