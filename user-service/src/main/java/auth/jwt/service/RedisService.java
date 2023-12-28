package auth.jwt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

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

    public void setRedisTemplate(String key, String value, Date now) {
        if (getRedisTemplateValue(key) != null) {
            deleteRedisTemplateValue(key);
        }

        LocalDateTime currentDateTime = now.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime futureDateTime = currentDateTime.plusDays(3); // 3일 뒤에 만료

        Duration expiredDuration = Duration.between(currentDateTime, futureDateTime);


        redisTemplate.opsForValue().set(key, value, expiredDuration);
    }

    public Boolean isExistKey(String key) {
        return redisTemplate.hasKey(key);
    }
}


