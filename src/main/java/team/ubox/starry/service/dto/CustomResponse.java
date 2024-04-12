package team.ubox.starry.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomResponse<T> {
    private Integer code;
    private String message;
    private T data;

    public CustomResponse(T data) {
        this(200, null, data);
    }
}
