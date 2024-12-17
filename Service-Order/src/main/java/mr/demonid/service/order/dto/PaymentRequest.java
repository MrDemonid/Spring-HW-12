package mr.demonid.service.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Запрос на перевод средств пользователя на счет магазина.
 */
@Data
@AllArgsConstructor
public class PaymentRequest {
    private UUID orderId;
    private Long fromUserId;
    private BigDecimal transferAmount;
    private String paymentMethod;
    private String type;            // "DEBIT", "CREDIT" и тд.
}
