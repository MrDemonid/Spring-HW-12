package mr.demonid.service.order.saga;

import lombok.AllArgsConstructor;
import mr.demonid.service.order.domain.Order;
import mr.demonid.service.order.domain.OrderItem;
import mr.demonid.service.order.domain.OrderStatus;
import mr.demonid.service.order.exceptions.SagaStepException;
import mr.demonid.service.order.repository.OrderRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Шаг: создание заказа.
 */
@AllArgsConstructor
public class CreateOrderStep implements SagaStep<SagaContext> {

    private final OrderRepository orderRepository;

    @Override
    @Transactional(rollbackFor = SagaStepException.class)
    public void execute(SagaContext context) throws SagaStepException {

        Order order = Order.builder()
                .userId(context.getUserId())
                .totalPrice(context.getTotalAmount())
                .paymentMethod(context.getPaymentMethod())
                .createAt(LocalDateTime.now())
                .status(OrderStatus.Pending)
                .build();
        final Order finalOrder = order;
        List<OrderItem> orderItems = context.getItems().stream()
                .map(e -> OrderItem.builder()
                        .order(finalOrder)
                        .productId(e.getProductId())
                        .productName(e.getProductName())
                        .quantity(e.getQuantity())
                        .price(e.getPrice())
                        .build()
                ).toList();
        order.setItems(orderItems);
        order = orderRepository.save(order);
        if (order.getOrderId() == null) {
            throw new SagaStepException("Ошибка создания заказа");      // ошибка создания заказа, возможно БД недоступна
        }
    }

    @Override
    public void rollback(SagaContext context) {
        if (context.getOrderId() != null) {
            Order order = orderRepository.findById(context.getOrderId()).orElse(null);
            if (order != null) {
                // меняем статус заказа на "отменен"
                order.setStatus(OrderStatus.Cancelled);
                order = orderRepository.save(order);
            }
            context.setOrderId(null);
        }
    }
}
