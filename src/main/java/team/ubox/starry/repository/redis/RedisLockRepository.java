package team.ubox.starry.repository.redis;

public interface RedisLockRepository {
    Boolean lock(final String key);
    Boolean unlock(final String key);
}
