package team.ubox.starry.security.filter.entrypoint;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import team.ubox.starry.exception.StarryError;
import team.ubox.starry.exception.StarryException;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final HandlerExceptionResolver handlerExceptionResolver;
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        //response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        handlerExceptionResolver.resolveException(request, response, null, new StarryException(StarryError.NEED_TOKEN));
    }

    private void sendErrorResponse(HttpServletResponse response) {

    }

    private void checkRefreshToken(HttpServletRequest request) {

    }
}