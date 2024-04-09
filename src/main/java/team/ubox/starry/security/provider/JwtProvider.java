package team.ubox.starry.security.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import team.ubox.starry.service.dto.user.LoginDTO;
import team.ubox.starry.repository.entity.User;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtProvider implements InitializingBean {
    private SecretKey key;
    private final String secret;

    public JwtProvider(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public LoginDTO.Response createToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        return new LoginDTO.Response(createAccessToken(user), createRefreshToken(user));
    }

    public String createAccessToken(User user) {
        long now = (new Date()).getTime();

        Date accessTokenExpireIn = new Date(now + (3600 * 12 * 1000));

        String accessToken = Jwts.builder().subject(user.getUsername())
                .id(user.getId().toString())
                .claim("auth", user.getUserRole())
                .expiration(accessTokenExpireIn)
                .signWith(key).compact();

        return accessToken;
    }

    private String createRefreshToken(User user) {
        long now = (new Date()).getTime();

        Date refreshTokenExpireIn = new Date(now + (3600 * 24 * 1000 * 7));
        String refreshToken = Jwts.builder()
                .id(user.getIdString())
                .expiration(refreshTokenExpireIn)
                .signWith(key).compact();

        return refreshToken;
    }

    public Authentication getAuthentication(String accessToken) {
        accessToken = resolveToken(accessToken);
        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(accessToken).getPayload();

        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new).toList();

        UserDetails user = User.builder()
                .id(UUID.fromString(claims.getId()))
                .username(claims.getSubject())
                .userRole(claims.get("auth").toString())
                .build();

        return new UsernamePasswordAuthenticationToken(user, "", authorities);
    }

    private String resolveToken(String bearerToken) {
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

    public boolean validateToken(String token) {
        token = resolveToken(token);
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String validateRefreshToken(String token) {
        token = resolveToken(token);
        try {
            Jws<Claims> res = Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            Claims claims = res.getPayload();
            return claims.getId();
        } catch (Exception e) {
            return null;
        }
    }
}
