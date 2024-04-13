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
    private String password;
    private String nickname;
    private String userRole;

    public static CustomUserDetail from(User entity) {
        CustomUserDetail instance = CustomUserDetail.builder().id(entity.getId()).username(entity.getUsername())
                .password(entity.getPassword()).nickname(entity.getNickname()).userRole(entity.getUserRole()).build();

        return instance;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(userRole.split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
