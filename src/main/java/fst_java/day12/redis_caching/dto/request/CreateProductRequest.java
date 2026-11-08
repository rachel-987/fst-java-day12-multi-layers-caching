package fst_java.day12.redis_caching.dto.request;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreateProductRequest(

        @NotBlank(message = "Product name is required")
        @Size(max = 200, message = "Name must not exceed 200 characters")
        String name,

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be greater than zero")
        @Digits(
                integer = 10,
                fraction = 2,
                message = "Invalid price format"
        )
        BigDecimal price

) {
}