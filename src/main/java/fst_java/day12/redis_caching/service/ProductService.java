package fst_java.day12.redis_caching.service;

import fst_java.day12.redis_caching.dto.request.CreateProductRequest;
import fst_java.day12.redis_caching.dto.response.ProductResponse;
import java.util.List;

public interface ProductService {
    ProductResponse createProduct(CreateProductRequest request);

    ProductResponse getById(Long id);

    List<ProductResponse> getAll();

    void deleteById(Long id);
}
