package auth.jwt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Transactional
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    public String getRedisTemplateValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteRedisTemplateValue(String key) {
        redisTemplate.delete(key);
    }

    public void setRedisTemplate(String key, String value, long time) {
        if (getRedisTemplateValue(key) != null) {
            deleteRedisTemplateValue(key);
        }

        Duration expiredDuration = Duration.ofDays(time);
        redisTemplate.opsForValue().set(key, value, expiredDuration);
    }

    public Boolean isExistKey(String key) {
        return redisTemplate.hasKey(key);
    }
}
