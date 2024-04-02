package team.ubox.starry.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import team.ubox.starry.entity.User;

import java.util.Optional;

public class AuthUtil {
    // Prevent create instance
    private AuthUtil() {}

    public static Optional<User> getAuthUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        return Optional.of((User) authentication.getPrincipal());
    }


}
