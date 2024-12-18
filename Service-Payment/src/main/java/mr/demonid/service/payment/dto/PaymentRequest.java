package mr.demonid.service.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import mr.demonid.service.payment.services.strategy.PaymentStrategy;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Запрос на перевод средств пользователя на счет магазина.
 */
@Data
@AllArgsConstructor
public class PaymentRequest {
    private UUID orderId;
    private Long userId;
    private BigDecimal transferAmount;
    private String paymentMethod;
    private String type;            // "DEBIT", "CREDIT" и тд.
}
