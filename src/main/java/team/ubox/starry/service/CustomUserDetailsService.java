package team.ubox.starry.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import team.ubox.starry.entity.User;
import team.ubox.starry.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> findUserResult = userRepository.findByUsername(username);

        if(findUserResult.isEmpty()) {
            throw new UsernameNotFoundException("존재하지 않는 사용자: " + username);
        }

        return findUserResult.get();
    }
}
