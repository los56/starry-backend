package team.ubox.starry.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

@Getter
public class StarryException extends RuntimeException {
    private final String errorType;
    private final HttpStatusCode status;

    @Setter
    private String message;

    public StarryException(StarryError error) {
        this.errorType = error.getErrorType();
        this.message = error.getMessage();
        this.status = HttpStatusCode.valueOf(error.getDefaultStatus());
    }

    public static StarryException RequireParameterException(String parameter) {
        StarryException e = new StarryException(StarryError.REQUIRE_PARAMETER);
        e.setMessage(String.format(e.message, parameter));

        return e;
    }

    public static StarryException MethodArgumentNotValidException(String argName, String message) {
        StarryException e = new StarryException(StarryError.INVALID_BODY);
        e.setMessage(String.format(e.message, argName, message));

        return e;
    }
}
