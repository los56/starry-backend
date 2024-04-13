package team.ubox.starry.helper;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import team.ubox.starry.repository.entity.CustomUserDetail;
import team.ubox.starry.repository.entity.User;

import java.util.Optional;

public class AuthHelper {
    // Prevent create instance
    private AuthHelper() {}

    public static Optional<CustomUserDetail> getAuthUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        return Optional.of((CustomUserDetail) authentication.getPrincipal());
    }


}
