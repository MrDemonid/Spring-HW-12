package mr.demonid.service.payment.services.steps;

import lombok.AllArgsConstructor;
import mr.demonid.service.payment.domain.PaymentEntity;
import mr.demonid.service.payment.dto.PaymentContext;
import mr.demonid.service.payment.exceptions.PaymentStepException;
import mr.demonid.service.payment.repository.PaymentRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Шаг: собственно списание средств, через выбранную систему оплаты.
 */
@AllArgsConstructor
public class PaymentExecutionStep implements PaymentStep<PaymentContext> {


    @Override
    public void execute(PaymentContext context) throws PaymentStepException {
        context.getPaymentStrategy().pay(context.getUserId(), context.getAmount());
    }

    @Override
    public void rollback(PaymentContext paymentData) {

    }
}
