package mr.demonid.service.payment.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mr.demonid.service.payment.domain.strategy.PaymentStrategy;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class PaymentData {
    private String userId;
    private BigDecimal amount;
    private boolean success;
    private String message;
    private PaymentStrategy paymentStrategy;

    public PaymentData(String userId, BigDecimal amount, PaymentStrategy paymentStrategy) {
        this.userId = userId;
        this.amount = amount;
        this.paymentStrategy = paymentStrategy;
        this.success = true; // Начальное состояние
    }
}

