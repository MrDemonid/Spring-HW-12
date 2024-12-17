package mr.demonid.service.order.saga;

import lombok.Data;
import mr.demonid.service.order.domain.OrderStatus;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Контекст для передачи данных между шагами.
 */
@Data
public class SagaContext {
    private UUID orderId;
    private long userId;
    private long productId;
    private int quantity;
    private BigDecimal price;
    private String paymentMethod;
    private OrderStatus status;

}
