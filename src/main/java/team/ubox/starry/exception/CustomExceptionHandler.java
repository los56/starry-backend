package team.ubox.starry.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import team.ubox.starry.service.dto.CustomResponse;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomResponse<String>> handleException(CustomException e, HttpServletRequest request) {
        return ResponseEntity.status(e.getStatus()).body(new CustomResponse<>(e.getStatus().value(), e.getMessage(), e.getErrorType()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<CustomResponse<String>> handleRequestParameterException(MissingServletRequestParameterException e) {
        CustomException sE = CustomException.RequireParameterException(e.getParameterName());
        return ResponseEntity.status(sE.getStatus()).body(new CustomResponse<>(sE.getStatus().value(), sE.getMessage(), sE.getErrorType()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomResponse<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        FieldError fieldError =  e.getBindingResult().getFieldErrors()
                .stream().findFirst().orElse(new FieldError("","",""));
        
        CustomException sE = CustomException.MethodArgumentNotValidException(fieldError.getField(), fieldError.getDefaultMessage());
        return ResponseEntity.status(400).body(new CustomResponse<>(sE.getStatus().value(), sE.getMessage(), sE.getErrorType()));
    }
}
