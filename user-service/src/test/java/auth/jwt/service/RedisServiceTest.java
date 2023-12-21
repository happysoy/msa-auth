package auth.jwt.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RedisServiceTest { //Redis 는 Transaction 처리를 따로 해줘야 함

    @Autowired
    private RedisService redisService;

    @Test
    void setRedis() {
        String key = "who";
        String value = "hello-world";

        long expiredTime = 60 * 1000; // 1분
        redisService.setRedisTemplate(key, value, expiredTime);
    }

    @Test
    void getRedis() {
        String key = "who";
        String value = "hello-world";

        String returnValue = redisService.getRedisTemplateValue(key);

        Assertions.assertThat(value).isEqualTo(returnValue);

    }

    @Test
    void isExistKey() {
        String key = "who";
        Boolean existKey = redisService.isExistKey(key);

        Assertions.assertThat(existKey).isTrue();

    }

}