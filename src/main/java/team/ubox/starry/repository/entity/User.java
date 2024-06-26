package team.ubox.starry.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import team.ubox.starry.types.UserRole;
import team.ubox.starry.helper.UUIDHelper;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(length = 128)
    private String username;

    private String password;

    @Column(length = 64)
    private String nickname;

    private String email;
    private String userRole;
    private Timestamp createDate;
    private Timestamp passwordChangeDate;

    private String profileImageUrl;

    public String getIdString() {
        return UUIDHelper.UUIDToString(this.id);
    }

    public void updateRole(UserRole[] roles) {
        this.userRole = Arrays.stream(roles).map(UserRole::getValue).collect(Collectors.joining(","));
    }

    public void updatePassword(String password, Timestamp passwordChangeDate) {
        this.password = password;
        this.passwordChangeDate = passwordChangeDate;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateProfileImageUrl(String url) {
        this.profileImageUrl = url;
    }
}
