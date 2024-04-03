package team.ubox.starry.exception;

public class InvalidTokenException extends StarryException {
    public InvalidTokenException() {
        super(StarryError.INVALID_TOKEN);
    }
}
