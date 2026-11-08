package fst_java.day12.redis_caching.event;

import com.github.benmanes.caffeine.cache.Cache;
import fst_java.day12.redis_caching.cache.ProductCacheEventPublisher;
import fst_java.day12.redis_caching.cache.ProductRedisCacheEvictor;
import fst_java.day12.redis_caching.dto.response.ProductResponse;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class ProductDeletionListener {

    private final ProductRedisCacheEvictor redisCacheEvictor;
    private final Cache<Long, ProductResponse> l1Cache;
    private final ProductCacheEventPublisher eventPublisher;

    public ProductDeletionListener(
            ProductRedisCacheEvictor redisCacheEvictor,
            Cache<Long, ProductResponse> l1Cache,
            ProductCacheEventPublisher eventPublisher
    ) {
        this.redisCacheEvictor = redisCacheEvictor;
        this.l1Cache = l1Cache;
        this.eventPublisher = eventPublisher;
    }

    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handle(ProductDeletedEvent event) {

        Long productId = event.productId();

        // 1. Xóa Redis L2
        redisCacheEvictor.evict(productId);

        // 2. Xóa Caffeine L1 tại instance hiện tại
        l1Cache.invalidate(productId);

        // 3. Phát thông báo cho tất cả instances
        eventPublisher.publishDeletion(productId);
    }
}