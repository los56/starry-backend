package team.ubox.starry.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import team.ubox.starry.service.dto.StarryResponse;

@RestControllerAdvice
public class StarryExceptionHandler {
    @ExceptionHandler(StarryException.class)
    public ResponseEntity<StarryResponse<String>> handleException(StarryException e, HttpServletRequest request) {
        return ResponseEntity.status(e.getStatus()).body(new StarryResponse<>(e.getStatus().value(), e.getMessage(), e.getErrorType()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<StarryResponse<String>> handleRequestParameterException(MissingServletRequestParameterException e) {
        StarryException sE = StarryException.RequireParameterException(e.getParameterName());
        return ResponseEntity.status(sE.getStatus()).body(new StarryResponse<>(sE.getStatus().value(), sE.getMessage(), sE.getErrorType()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StarryResponse<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        FieldError fieldError =  e.getBindingResult().getFieldErrors()
                .stream().findFirst().orElse(new FieldError("","",""));
        
        StarryException sE = StarryException.MethodArgumentNotValidException(fieldError.getField(), fieldError.getDefaultMessage());
        return ResponseEntity.status(400).body(new StarryResponse<>(sE.getStatus().value(), sE.getMessage(), sE.getErrorType()));
    }
}
