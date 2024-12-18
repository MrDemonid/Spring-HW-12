package mr.demonid.service.order.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Таблица заказов.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id", updatable = false, nullable = false)
    private UUID orderId;

    @Column(nullable = false)
    private long userId;

    @Column(nullable = false)
    private BigDecimal totalPrice;

    @Column(nullable = false)
    private String paymentMethod;

    private LocalDateTime createAt;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    List<OrderItem> items;

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        Order order = (Order) o;
//        return userId == order.userId && productId == order.productId && quantity == order.quantity && Objects.equals(orderId, order.orderId) && Objects.equals(price, order.price) && Objects.equals(orderDate, order.orderDate) && status == order.status;
//    }
//
//    @Override
//    public int hashCode() {
//        int result = Objects.hashCode(orderId);
//        result = 31 * result + Long.hashCode(userId);
//        result = 31 * result + Long.hashCode(productId);
//        result = 31 * result + quantity;
//        result = 31 * result + Objects.hashCode(price);
//        result = 31 * result + Objects.hashCode(paymentMethod);
//        result = 31 * result + Objects.hashCode(orderDate);
//        result = 31 * result + Objects.hashCode(status);
//        return result;
//    }
}
