package mr.demonid.service.payment.services.steps;

import lombok.AllArgsConstructor;
import mr.demonid.service.payment.domain.PaymentEntity;
import mr.demonid.service.payment.domain.PaymentStatus;
import mr.demonid.service.payment.domain.UserEntity;
import mr.demonid.service.payment.dto.PaymentContext;
import mr.demonid.service.payment.exceptions.PaymentStepException;
import mr.demonid.service.payment.repository.PaymentRepository;
import mr.demonid.service.payment.repository.UserRepository;

import java.math.BigDecimal;
import java.util.Optional;


/**
 * Шаг: проверка баланса.
 */
@AllArgsConstructor
public class CheckBalanceStep implements PaymentStep<PaymentContext> {

    private PaymentRepository paymentRepository;
    private UserRepository userRepository;


    @Override
    public void execute(PaymentContext context) throws PaymentStepException {
        Optional<UserEntity> user = userRepository.findById(context.getUserId());
        if (user.isEmpty()) {
            throw new PaymentStepException("Пользователь не найден");
        }
        BigDecimal balance = user.get().getAmount();
        if (context.getAmount().compareTo(balance) > 0) {
            throw new PaymentStepException("Недостаточно средств");
        }
    }

    /**
     * Операция не удалась, помечаем её как отмененную.
     */
    @Override
    public void rollback(PaymentContext context) {
        Optional<PaymentEntity> entity = paymentRepository.findByOrderId(context.getOrderId());
        if (entity.isPresent()) {
            PaymentEntity payment = entity.get();
            payment.setStatus(PaymentStatus.Cancelled);
            paymentRepository.save(payment);
        }
    }
}
