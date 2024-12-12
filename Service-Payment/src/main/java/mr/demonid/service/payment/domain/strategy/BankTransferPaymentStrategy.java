package mr.demonid.service.payment.domain.strategy;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BankTransferPaymentStrategy implements PaymentStrategy {
    @Override
    public void pay(String userId, BigDecimal amount) {
        System.out.println("Оплата " + amount + " через банк для пользователя: " + userId);
    }
}
