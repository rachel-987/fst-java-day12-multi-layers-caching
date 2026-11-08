package fst_java.day12.redis_caching.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import fst_java.day12.redis_caching.dto.response.ProductResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CaffeineConfig {

    @Bean
    public Cache<Long, ProductResponse> productL1Cache() {
        /*
            Spring sẽ gọi phương thức để tạo Caffeine Cache và đăng ký object đó trong IoC Container.
            Sau này, một Spring Bean khác cần Cache<Long, ProductResponse> thì Spring có thể inject object này qua constructor.
         */

        // L1 có TTL ngắn hơn L2 để hạn chế thời gian giữ bản cache cũ khi có thay đổi dữ liệu.
        return Caffeine.newBuilder()
                .maximumSize(1000)  // Giới hạn dung lượng cache ở khoảng 1.000 entries. Caffeine có thể tự loại bỏ entries khi cần.
                .expireAfterWrite(Duration.ofSeconds(60))   // Dữ liệu hết hạn sau 60 giây kể từ lần ghi hoặc cập nhật vào cache.
                .build();
    }
}