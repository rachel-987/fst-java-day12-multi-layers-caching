package fst_java.day12.redis_caching.dto.response;

import java.math.BigDecimal;

public record ProductResponse(Long id, String name, BigDecimal price) {
}