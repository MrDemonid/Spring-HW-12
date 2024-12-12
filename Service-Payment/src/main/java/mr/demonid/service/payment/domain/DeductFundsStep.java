package mr.demonid.service.payment.domain;

import mr.demonid.service.payment.dto.PaymentData;
import org.springframework.stereotype.Component;

/**
 * Резервирование средств.
 */
@Component
public class DeductFundsStep implements PaymentStep {
    @Override
    public void execute(PaymentData data) {
        if (!data.isSuccess())
            return;             // -> пред. шаг был неудачен, уходим
        System.out.println("Вычитание " + data.getAmount() + " у пользователя: " + data.getUserId());
    }
}
