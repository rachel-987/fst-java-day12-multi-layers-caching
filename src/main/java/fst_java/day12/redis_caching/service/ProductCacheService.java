package fst_java.day12.redis_caching.service;

import com.github.benmanes.caffeine.cache.Cache;
import fst_java.day12.redis_caching.dto.response.ProductResponse;
import org.springframework.stereotype.Service;

@Service
public class ProductCacheService {

    private final Cache<Long, ProductResponse> l1Cache;
    private final ProductService productService;

    /*
        Spring inject hai dependency:
        - l1Cache: Bean được tạo trong CaffeineConfig.
        - productService: Bean ProductServiceImpl đang implement interface ProductService.
     */
    public ProductCacheService(Cache<Long, ProductResponse> l1Cache, ProductService productService) {
        this.l1Cache = l1Cache;
        this.productService = productService;
    }

    public ProductResponse getById(Long id) {

        // 1. Kiểm tra L1
        ProductResponse cached = l1Cache.getIfPresent(id);

        if (cached != null) {
            System.out.println("[L1 HIT] Product ID: " + id);
            return cached;
        }

        System.out.println("[L1 MISS] Product ID: " + id);

        // 2. Đọc từ L2 Redis hoặc PostgreSQL
        ProductResponse response = productService.getById(id);

        // 3. Lưu lại vào L1
        l1Cache.put(id, response);

        return response;
    }
}