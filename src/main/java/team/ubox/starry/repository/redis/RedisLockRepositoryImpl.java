package team.ubox.starry.repository.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class RedisLockRepositoryImpl implements RedisLockRepository {
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * key: 접근하고자 하는 데이터의 키
     * return: Lock 획득 여부
     * **/
    public Boolean lock(final String key) {
        // key 를 바로 사용하면 무조건 존재하므로 Suffix 를 붙임
        return redisTemplate.opsForValue().setIfAbsent(key + "_LOCK", "lock", Duration.ofSeconds(3));
    }

    public Boolean unlock(final String key) {
        return redisTemplate.delete(key + "_LOCK");
    }
}
