package mr.demonid.service.order.services;

import feign.FeignException;
import lombok.AllArgsConstructor;
import mr.demonid.service.order.domain.Order;
import mr.demonid.service.order.domain.OrderStatus;
import mr.demonid.service.order.dto.PaymentRequest;
import mr.demonid.service.order.dto.ProductReservationRequest;
import mr.demonid.service.order.exceptions.BadOrderException;
import mr.demonid.service.order.exceptions.OrderThrowedException;
import mr.demonid.service.order.links.CatalogServiceClient;
import mr.demonid.service.order.links.PaymentServiceClient;
import mr.demonid.service.order.repository.OrderRepository;
import mr.demonid.service.order.saga.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderService {

    private OrderRepository orderRepository;

    private CatalogServiceClient catalogServiceClient;
    private PaymentServiceClient paymentServiceClient;

    /**
     * Создаёт новый заказ и проводит его через все этапы.
     * @param userId        Заказчик.
     * @param productId     Код товара.
     * @param quantity      Количество.
     * @param price         Стоимость за единицу.
     * @param paymentMethod Тип оплаты.
     * @return Идентификатор заказа, или NULL в случае неудачи (например отказ БД).
     */
    public UUID createOrder(long userId, long productId, int quantity, BigDecimal price, String paymentMethod) {
        // Создаём контекст данных заказа.
        SagaContext context = new SagaContext();
        context.setUserId(userId);
        context.setProductId(productId);
        context.setQuantity(quantity);
        context.setPrice(price);
        context.setPaymentMethod(paymentMethod);

        // Задаем последовательность действий.
        SagaOrchestrator<SagaContext> orchestrator = new SagaOrchestrator<>();
        orchestrator.addStep(new CreateOrderStep(orderRepository));                 // открываем заказ
        orchestrator.addStep(new ProductReservationStep(catalogServiceClient));     // резервируем товар
        orchestrator.addStep(new PaymentReservationStep(paymentServiceClient));     // проверяем средства и способ оплаты
        orchestrator.addStep(new ApprovedStep(orderRepository, catalogServiceClient, paymentServiceClient));    // завершение сделки
        orchestrator.addStep(new InformationStep());                                // оповещаем пользователя

        // Запускаем выполнение и возвращаем результат.
        orchestrator.execute(context);
        return context.getOrderId();
    }

    /**
     * Возвращает список всех находящихся в обработке заказов.
     */
    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

}
