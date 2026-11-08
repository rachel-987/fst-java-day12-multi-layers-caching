package fst_java.day12.redis_caching.controller;

import fst_java.day12.redis_caching.dto.request.CreateProductRequest;
import fst_java.day12.redis_caching.dto.response.ProductResponse;
import fst_java.day12.redis_caching.service.ProductCacheService;
import fst_java.day12.redis_caching.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Product API",
        description = "Quản lý sản phẩm và kiểm thử Redis Cache"
)

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final ProductCacheService productCacheService;

    public ProductController(ProductService productService, ProductCacheService productCacheService) {
        this.productService = productService;
        this.productCacheService = productCacheService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Tạo sản phẩm mới")
    public ProductResponse create(
            @Valid @RequestBody CreateProductRequest request) {

        return productService.createProduct(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy sản phẩm theo ID")
    public ProductResponse getById(@PathVariable Long id) {
        return productService.getById(id);
    }

    @Operation(summary = "Lấy sản phẩm theo ID với L1/L2 Cache")
    @GetMapping("/v2/{id}")
    public ProductResponse getByIdV2(@PathVariable Long id) {

        return productCacheService.getById(id);
    }

    @GetMapping()
    @Operation(summary = "Lấy tất cả")
    public List<ProductResponse> getAll() {
        return productService.getAll();
    }

    @Operation(summary = "Xóa sản phẩm theo ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {

        productService.deleteById(id);
    }
}