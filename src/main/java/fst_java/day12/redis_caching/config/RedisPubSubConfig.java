package fst_java.day12.redis_caching.config;

import fst_java.day12.redis_caching.cache.ProductCacheEventPublisher;
import fst_java.day12.redis_caching.cache.ProductCacheInvalidationSubscriber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
public class RedisPubSubConfig {

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory connectionFactory,
            ProductCacheInvalidationSubscriber subscriber
    ) {

        // RedisMessageListenerContainer: chịu trách nhiệm quản lý subscription và phân phối message tới listener.
        /*
            NOTE: Một instance Spring Boot được khởi động sẽ tạo một RedisMessageListenerContainer riêng.
            --- Vì cả hai instance đều đăng ký cùng channel, chúng sẽ cùng nhận được message.
         */
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();

        container.setConnectionFactory(connectionFactory);

        container.addMessageListener(subscriber, new ChannelTopic(
                ProductCacheEventPublisher.CHANNEL
        ));

        return container;
    }
}