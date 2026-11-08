package fst_java.day12.redis_caching.event;

/*
    Đây là một Application Event.
    - Nó chỉ mang thông tin sản phẩm đã bị xóa, không thực hiện thao tác database hay cache.
    ??? Tại sao phải có Event?
    ---- Vì chúng ta muốn tách nghiệp vụ xóa Product khỏi việc xử lý cache
    ---- chỉ invalidate khi database commit thành công.
 */
public record ProductDeletedEvent(Long productId) {
}
