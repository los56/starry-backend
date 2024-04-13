package team.ubox.starry.repository.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CustomUserDetail implements UserDetails {
    private UUID id;
    private String username;
    private String nickname;
    private String userRole;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(userRole.split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
