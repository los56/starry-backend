package team.ubox.starry.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import team.ubox.starry.repository.entity.CustomUserDetail;
import team.ubox.starry.repository.entity.User;
import team.ubox.starry.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User foundUser = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        CustomUserDetail userDetail = CustomUserDetail.builder()
                .id(foundUser.getId()).username(foundUser.getUsername()).password(foundUser.getPassword())
                .userRole(foundUser.getUserRole()).build();

        return userDetail;
    }
}
