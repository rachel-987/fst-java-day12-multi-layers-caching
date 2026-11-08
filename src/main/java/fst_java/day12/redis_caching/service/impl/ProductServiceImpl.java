package fst_java.day12.redis_caching.service.impl;

import fst_java.day12.redis_caching.dto.request.CreateProductRequest;
import fst_java.day12.redis_caching.dto.response.ProductResponse;
import fst_java.day12.redis_caching.entity.Product;
import fst_java.day12.redis_caching.event.ProductDeletedEvent;
import fst_java.day12.redis_caching.mapper.ProductMapper;
import fst_java.day12.redis_caching.repository.ProductRepository;
import fst_java.day12.redis_caching.service.ProductService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ApplicationEventPublisher eventPublisher;

    // Constructor Injection
    public ProductServiceImpl(
            ProductRepository productRepository,
            ProductMapper productMapper, ApplicationEventPublisher eventPublisher
    ) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {
        Product product = productMapper.toEntity(request);

        Product savedProduct = productRepository.save(product);

        return productMapper.toResponse(savedProduct);
    }

    /*
        cacheNames = "products" xác định tên cache.
        key = "#p0" nghĩa là dùng tham số đầu tiên của phương thức, ở đây là id, làm cache key.
        --- Với prefix đã cấu hình, Redis key của Product ID 1 sẽ là lesson12:products::1.
    */
    @Override
    @Cacheable(cacheNames = "products", key = "#p0")    //xác định tên cache
    @Transactional(readOnly = true)
    public ProductResponse getById(Long id) {

        System.out.println("QUERY DATABASE: " + id);

        Product product = productRepository.findById(id)
                .orElseThrow();

        return productMapper.toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAll() {

        return productRepository.findAll()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow();

        //Yêu cầu Hibernate xóa sản phẩm khỏi PostgreSQL trong transaction hiện tại.
        productRepository.delete(product);

        /*
            - Phát một Spring Application Event (không phải Redis Pub/Sub)
            - Event này sẽ được listener xử lý sau khi DB commit.
         */
        eventPublisher.publishEvent(
                new ProductDeletedEvent(id)
        );
    }
}