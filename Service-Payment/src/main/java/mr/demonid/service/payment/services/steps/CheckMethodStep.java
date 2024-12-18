package mr.demonid.service.payment.services.steps;

import lombok.AllArgsConstructor;
import mr.demonid.service.payment.dto.PaymentContext;
import mr.demonid.service.payment.exceptions.PaymentStepException;
import mr.demonid.service.payment.services.strategy.PaymentStrategy;

/**
 * Шаг: проверка доступности метода оплаты.
 */
@AllArgsConstructor
public class CheckMethodStep implements PaymentStep<PaymentContext> {

    @Override
    public void execute(PaymentContext context) throws PaymentStepException {
        PaymentStrategy paymentStrategy = context.getPaymentStrategy();
        if (paymentStrategy == null || !paymentStrategy.isPresent()) {
            throw new PaymentStepException("Выбранный способ оплаты недоступен");
        }
    }

    @Override
    public void rollback(PaymentContext paymentData) {

    }
}
