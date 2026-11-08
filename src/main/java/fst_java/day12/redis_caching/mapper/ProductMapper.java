package fst_java.day12.redis_caching.mapper;

import fst_java.day12.redis_caching.dto.request.CreateProductRequest;
import fst_java.day12.redis_caching.dto.response.ProductResponse;
import fst_java.day12.redis_caching.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(CreateProductRequest request) {
        return new Product(
                request.name(),
                request.price()
        );
    }

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice()
        );
    }
}