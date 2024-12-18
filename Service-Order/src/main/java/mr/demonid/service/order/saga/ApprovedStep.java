package mr.demonid.service.order.saga;

import feign.FeignException;
import lombok.AllArgsConstructor;
import mr.demonid.service.order.domain.Order;
import mr.demonid.service.order.domain.OrderStatus;
import mr.demonid.service.order.exceptions.SagaStepException;
import mr.demonid.service.order.links.CatalogServiceClient;
import mr.demonid.service.order.links.PaymentServiceClient;
import mr.demonid.service.order.repository.OrderRepository;

/**
 * Шаг: завершаем сделку.
 */
@AllArgsConstructor
public class ApprovedStep implements SagaStep<SagaContext> {

    private OrderRepository orderRepository;
    private CatalogServiceClient catalogServiceClient;


    @Override
    public void execute(SagaContext context) throws SagaStepException {
        try {
            // Подгружаем данные из БД, пользуясь ленивой дефолтной загрузкой (поскольку зависимости нам не нужны)
            catalogServiceClient.approve(context.getOrderId());
            Order order = orderRepository.findById(context.getOrderId()).orElse(null);
            if (order != null) {
                // меняем статус
                order.setStatus(OrderStatus.Approved);
                orderRepository.save(order);
            }
        } catch (FeignException e) {
            throw new SagaStepException("Покупка не состоялась: " + e.getMessage());
        } catch (RuntimeException e) {
            throw new SagaStepException("Непредвиденная ошибка с БД: " + e.getMessage());
        }
    }

    @Override
    public void rollback(SagaContext context) {
        // Если после подтверждения резерва делать небольшую паузу, то
        // этот метод вполне сработает.
        // Ну или отменять резерв забирая товар уже у службы доставки,
        // тут все от реализации зависит, а здесь это не важно.
        try {
            catalogServiceClient.unblock(context.getOrderId());
        } catch (FeignException ignored) {}
    }

}