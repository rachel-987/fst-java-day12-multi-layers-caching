package fst_java.day12.redis_caching.cache;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

@Component
public class ProductRedisCacheEvictor {

    @CacheEvict(cacheNames = "products", key = "#p0")

    // method evict ko gọi Redis trực tiếp
    /*
        Khi một Bean khác gọi evict()
        -> Spring run method này
        -> xóa key tương ứng trong Redis CacheManager nếu thành công.
        *** Chúng ta tách thành một Bean riêng để lời gọi đi qua Spring Proxy, tránh lỗi self-invocation.
     */
    public void evict(Long productId) {

        System.out.println(
                "[L2 EVICT] Product ID: " + productId
        );
    }
}