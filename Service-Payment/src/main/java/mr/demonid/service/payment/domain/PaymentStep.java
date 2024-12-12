package mr.demonid.service.payment.domain;

import mr.demonid.service.payment.dto.PaymentData;

/**
 * Один шаг операции по оплате.
 */
public interface PaymentStep {
    void execute(PaymentData paymentData);
}
