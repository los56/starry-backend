package team.ubox.starry.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

@Getter
public class CustomException extends RuntimeException {
    private final String errorType;
    private final HttpStatusCode status;

    @Setter
    private String message;

    public CustomException(CustomError error) {
        this.errorType = error.getErrorType();
        this.message = error.getMessage();
        this.status = HttpStatusCode.valueOf(error.getDefaultStatus());
    }

    public static CustomException RequireParameterException(String parameter) {
        CustomException e = new CustomException(CustomError.REQUIRE_PARAMETER);
        e.setMessage(String.format(e.message, parameter));

        return e;
    }

    public static CustomException MethodArgumentNotValidException(String argName, String message) {
        CustomException e = new CustomException(CustomError.INVALID_BODY);
        e.setMessage(String.format(e.message, argName, message));

        return e;
    }
}
