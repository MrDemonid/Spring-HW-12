package mr.demonid.service.payment.domain;

import mr.demonid.service.payment.dto.PaymentData;
import org.springframework.stereotype.Component;

/**
 * Шаг: собственно списание средств, через выбранную систему оплаты.
 */
@Component
public class PaymentExecutionStep implements PaymentStep {
    @Override
    public void execute(PaymentData data) {
        if (!data.isSuccess()) return;
        data.getPaymentStrategy().pay(data.getUserId(), data.getAmount());
    }
}
