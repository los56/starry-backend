package team.ubox.starry.exception;

public class InvalidTokenException extends CustomException {
    public InvalidTokenException() {
        super(CustomError.INVALID_TOKEN);
    }
}
