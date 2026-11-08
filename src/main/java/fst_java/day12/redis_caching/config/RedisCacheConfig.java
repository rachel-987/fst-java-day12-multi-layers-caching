package fst_java.day12.redis_caching.config;

import fst_java.day12.redis_caching.dto.response.ProductResponse;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.*;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
@EnableCaching  //@EnableCaching kích hoạt cơ chế xử lý cache annotation thông qua Spring AOP.
public class RedisCacheConfig {

    @Bean
    /*
        Spring Boot tự cấu hình RedisConnectionFactory từ application.yaml,
        sau đó inject dependency vào phương thức @Bean.
        Nhờ đó:
        - Không cần tự new kết nối Redis.
        - RedisCacheManager được Spring IoC Container quản lý.
        - @Cacheable sẽ sử dụng CacheManager này.
     */

    /*
        Spring IoC Container quản lý RedisCacheManager
        @EnableCaching kích hoạt hạ tầng AOP phục vụ cache.
     */
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        var serializer =
                new JacksonJsonRedisSerializer<>(
                        ProductResponse.class
                );

        RedisCacheConfiguration config =
                RedisCacheConfiguration.defaultCacheConfig()
                        .serializeValuesWith(
                                RedisSerializationContext
                                        .SerializationPair
                                        .fromSerializer(serializer)
                        )
                        .entryTtl(Duration.ofMinutes(5))
                        .disableCachingNullValues()
                        .computePrefixWith(
                                name -> "lesson12:" + name + "::"
                        );

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .build();
    }
}