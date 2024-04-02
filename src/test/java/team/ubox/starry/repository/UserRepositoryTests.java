package team.ubox.starry.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import team.ubox.starry.entity.User;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Transactional
@SpringBootTest
public class UserRepositoryTests {
    private final UserRepository userRepository;

    @Autowired
    public UserRepositoryTests(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @BeforeEach
    void beforeAll() {
        Timestamp nowTimestamp = Timestamp.from(Instant.now());

        User user1 = new User(null, "test1234",
                "asdf", "nick1",
                "aa@aa.aa", "USER",
                nowTimestamp, nowTimestamp);

        User user2 = new User(null, "test12345",
                "asdf", "nick2",
                "bb@aa.aa", "USER",
                nowTimestamp, nowTimestamp);

        this.userRepository.save(user1);
        this.userRepository.save(user2);
    }

    @Test
    void findAll() {
        List<User> result = this.userRepository.findAll();
        Assertions.assertEquals(result.size(), 2);

        System.out.println("result.get(0) = " + result.get(0).getId());
    }

    @Test
    void findAll2() {
        List<User> result = this.userRepository.findAll();
        Assertions.assertEquals(result.size(), 2);

        System.out.println("result.get(0) = " + result.get(0).getId());
    }
}
