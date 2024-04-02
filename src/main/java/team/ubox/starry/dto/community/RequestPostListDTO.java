package team.ubox.starry.dto.community;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RequestPostListDTO {
    private String id;
    private Integer page;
    private Integer postPerPage;
    private Boolean isAscending = false;
}
