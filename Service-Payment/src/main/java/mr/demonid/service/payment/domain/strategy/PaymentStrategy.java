package mr.demonid.service.payment.domain.strategy;

import java.math.BigDecimal;

/**
 * Списание средств пользователя.
 */
public interface PaymentStrategy {
    void pay(String userId, BigDecimal amount);
    String getName();
}

