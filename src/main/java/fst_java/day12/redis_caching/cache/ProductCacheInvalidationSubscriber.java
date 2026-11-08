package fst_java.day12.redis_caching.cache;

import com.github.benmanes.caffeine.cache.Cache;
import fst_java.day12.redis_caching.dto.response.ProductResponse;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class ProductCacheInvalidationSubscriber implements MessageListener {
    // MessageListener: interface do Spring Data Redis cung cấp.
    // implement interface MessageListener để xác định khi nhận message thì phải làm gì.
    private final Cache<Long, ProductResponse> l1Cache;

    public ProductCacheInvalidationSubscriber(Cache<Long, ProductResponse> l1Cache) {
        this.l1Cache = l1Cache;
    }

    @Override
    // Spring không tự viết logic onMessage() cho nghiệp vụ Product. cần phải tự implement việc xóa L1.
    public void onMessage(Message message, byte[] pattern) {

        String payload = new String(
                message.getBody(),
                StandardCharsets.UTF_8
        );

        try {
            Long productId = Long.valueOf(payload);

            l1Cache.invalidate(productId);

            System.out.println(
                    "[SUBSCRIBER] L1 INVALIDATED: " + productId
            );

        } catch (NumberFormatException ex) {
            System.err.println(
                    "Invalid Product ID message: " + payload
            );
        }
    }
}