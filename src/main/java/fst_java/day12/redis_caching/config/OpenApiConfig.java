package fst_java.day12.redis_caching.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Info;

@Configuration  //Class khai báo cấu hình Spring
public class OpenApiConfig {

    @Bean   //Đăng ký object OpenAPI vào Spring IoC Container.
    //- Springdoc sử dụng bean này để xây dựng thông tin tài liệu API.
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Product Caching API")
                        .version("v1")
                        .description(
                                "Fast Track Java - Redis L1/L2 Caching Assignment"
                        )
                );
    }
}