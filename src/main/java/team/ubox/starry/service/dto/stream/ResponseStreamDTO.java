package team.ubox.starry.service.dto.stream;

import lombok.*;
import team.ubox.starry.service.dto.channel.ChannelDTO;
import team.ubox.starry.types.StreamStatus;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ResponseStreamDTO {
    private ChannelDTO.Response channel;
    private StreamStatus status;

    private String streamId;

    private String streamTitle;
    private String streamCategory;
}
