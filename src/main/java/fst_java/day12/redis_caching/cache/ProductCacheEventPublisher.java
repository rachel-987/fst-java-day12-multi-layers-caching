package fst_java.day12.redis_caching.cache;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProductCacheEventPublisher {

    public static final String CHANNEL =
            "lesson12:product-deletion";

    // StringRedisTemplate: là một Spring Bean do Spring Boot tự cấu hình khi có Spring Data Redis và cấu hình spring.data.redis.
    private final StringRedisTemplate redisTemplate;

    public ProductCacheEventPublisher(
            StringRedisTemplate redisTemplate
    ) {
        this.redisTemplate = redisTemplate;
    }

    public void publishDeletion(Long productId) {

        redisTemplate.convertAndSend(CHANNEL, productId.toString());    // ~ PUBLISH lesson12:product-deletion "3"
        // Nó gửi thông điệp tới những subscriber đang đăng ký channel đó.

        System.out.println(
                "[PUBLISH] Deleted product: " + productId
        );
    }
}