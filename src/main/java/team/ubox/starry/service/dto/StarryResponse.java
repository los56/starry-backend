package team.ubox.starry.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StarryResponse<T> {
    private Integer code;
    private String message;
    private T data;

    public StarryResponse(T data) {
        this(200, null, data);
    }
}
