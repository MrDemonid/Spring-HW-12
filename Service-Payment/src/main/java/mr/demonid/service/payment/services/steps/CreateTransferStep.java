package mr.demonid.service.payment.services.steps;

import lombok.AllArgsConstructor;
import mr.demonid.service.payment.domain.PaymentEntity;
import mr.demonid.service.payment.domain.PaymentStatus;
import mr.demonid.service.payment.dto.PaymentContext;
import mr.demonid.service.payment.exceptions.PaymentStepException;
import mr.demonid.service.payment.repository.PaymentRepository;
import mr.demonid.service.payment.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


/**
 * Шаг: открытие трансфера.
 */
@AllArgsConstructor
public class CreateTransferStep implements PaymentStep<PaymentContext> {

    private PaymentRepository paymentRepository;


    @Override
    @Transactional
    public void execute(PaymentContext context) throws PaymentStepException {
        // создаем в БД запись о транзакции
        PaymentEntity entity = new PaymentEntity(
                context.getOrderId(),
                context.getUserId(),
                context.getAmount(),
                context.getPaymentStrategy().getName(),
                PaymentStatus.Pending);
        paymentRepository.save(entity);
    }

    @Override
    public void rollback(PaymentContext context) {
        Optional<PaymentEntity> entity = paymentRepository.findByOrderId(context.getOrderId());
        if (entity.isPresent()) {
            entity.get().setStatus(PaymentStatus.Cancelled);
            paymentRepository.save(entity.get());
        }
    }
}
