package mr.demonid.service.payment.domain.strategy;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CreditCardPaymentStrategy implements PaymentStrategy {

    @Override
    public void pay(String userId, BigDecimal amount) {
        System.out.println("Using CreditCardPaymentStrategy");
    }

    @Override
    public String getName() {
        return "CreditCard";
    }
}
