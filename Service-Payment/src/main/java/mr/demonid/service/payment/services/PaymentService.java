package mr.demonid.service.payment.services;

import mr.demonid.service.payment.domain.PaymentStep;
import mr.demonid.service.payment.domain.strategy.PaymentStrategy;
import mr.demonid.service.payment.dto.PaymentData;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentService {
    private final List<PaymentStep> steps;

    public PaymentService(List<PaymentStep> steps) {
        this.steps = steps;
    }

    public void processPayment(String userId, BigDecimal amount, PaymentStrategy paymentStrategy) {
        PaymentData data = new PaymentData(userId, amount, paymentStrategy);

        for (PaymentStep step : steps) {
            step.execute(data);
            if (!data.isSuccess()) {
                System.out.println("Payment process stopped: " + data.getMessage());
                break; // Прерываем цепочку при ошибке
            }
        }

        if (data.isSuccess()) {
            System.out.println("Payment completed successfully for user: " + userId);
        }
    }
}
