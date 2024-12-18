package mr.demonid.service.payment.services.steps;

import mr.demonid.service.payment.exceptions.PaymentStepException;

/**
 * Один шаг операции по оплате.
 */
public interface PaymentStep<T> {
    void execute(T paymentData) throws PaymentStepException;
    void rollback(T paymentData);
}

