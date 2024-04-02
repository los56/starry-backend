package team.ubox.starry.dto.community;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ResponsePostListDTO {
    private Integer page;
    private Integer allPageCount;
    private Long allPostsCount;
    private List<ResponsePostDTO> posts;
}
