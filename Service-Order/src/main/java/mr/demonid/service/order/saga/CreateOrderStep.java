package mr.demonid.service.order.saga;

import lombok.AllArgsConstructor;
import mr.demonid.service.order.domain.Order;
import mr.demonid.service.order.domain.OrderStatus;
import mr.demonid.service.order.exceptions.SagaStepException;
import mr.demonid.service.order.repository.OrderRepository;

import java.time.LocalDateTime;

/**
 * Шаг: создание заказа.
 */
@AllArgsConstructor
public class CreateOrderStep implements SagaStep<SagaContext> {

    private final OrderRepository orderRepository;

    @Override
    public void execute(SagaContext context) throws SagaStepException {
        Order order = new Order(
                context.getUserId(),
                context.getProductId(),
                context.getQuantity(),
                context.getPrice(),
                context.getPaymentMethod(),
                LocalDateTime.now(),
                OrderStatus.Pending);
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
