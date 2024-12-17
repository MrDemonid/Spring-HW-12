package mr.demonid.service.order.saga;

import feign.FeignException;
import lombok.AllArgsConstructor;
import mr.demonid.service.order.dto.PaymentRequest;
import mr.demonid.service.order.exceptions.SagaStepException;
import mr.demonid.service.order.links.PaymentServiceClient;

import java.math.BigDecimal;

/**
 * Шаг: проверка и резервирование средств у пользователя.
 */
@AllArgsConstructor
public class PaymentReservationStep implements SagaStep<SagaContext> {

    private PaymentServiceClient paymentServiceClient;

    @Override
    public void execute(SagaContext context) throws SagaStepException {
        try {
            PaymentRequest paymentRequest = new PaymentRequest(
                    context.getOrderId(),
                    context.getUserId(),
                    context.getPrice().multiply(BigDecimal.valueOf(context.getQuantity())),
                    context.getPaymentMethod(),
                    "BUY");
            paymentServiceClient.reservation(paymentRequest);
        } catch (FeignException e) {
            throw new SagaStepException("Ошибка оплаты: " + e.getMessage());
        }
    }

    @Override
    public void rollback(SagaContext context) {
        try {
            paymentServiceClient.rollback(context.getOrderId());
        } catch (FeignException ignored) {}
    }

}
