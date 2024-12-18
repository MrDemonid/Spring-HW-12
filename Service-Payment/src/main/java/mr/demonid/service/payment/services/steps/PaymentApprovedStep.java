package mr.demonid.service.payment.services.steps;

import lombok.AllArgsConstructor;
import mr.demonid.service.payment.domain.PaymentEntity;
import mr.demonid.service.payment.domain.PaymentStatus;
import mr.demonid.service.payment.dto.PaymentContext;
import mr.demonid.service.payment.exceptions.PaymentStepException;
import mr.demonid.service.payment.repository.PaymentRepository;

import java.util.Optional;

/**
 * Шаг: завершение платежа, просто меняем статус на завершенный.
 */
@AllArgsConstructor
public class PaymentApprovedStep implements PaymentStep<PaymentContext> {

    PaymentRepository paymentRepository;


    @Override
    public void execute(PaymentContext context) throws PaymentStepException {
        Optional<PaymentEntity> entity = paymentRepository.findByOrderId(context.getOrderId());
        if (entity.isPresent()) {
            entity.get().setStatus(PaymentStatus.Approved);
            paymentRepository.save(entity.get());
        }
    }

    @Override
    public void rollback(PaymentContext context) {

    }
}
