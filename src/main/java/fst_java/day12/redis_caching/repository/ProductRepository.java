package fst_java.day12.redis_caching.repository;

import fst_java.day12.redis_caching.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/*
    Spring Data JPA tạo implementation cho repository. Khi chạy, Hibernate sử dụng JDBC để thực thi SQL trên PostgreSQL.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

}
