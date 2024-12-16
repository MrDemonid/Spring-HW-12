package mr.demonid.web.client.service;

import feign.FeignException;
import lombok.AllArgsConstructor;
import mr.demonid.web.client.links.PaymentServiceClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PaymentService {

    private PaymentServiceClient paymentServiceClient;

    public List<String> getPaymentStrategies() {
        try {
            List<String> res = paymentServiceClient.getAvailableStrategies().getBody();
            return res;
        } catch (FeignException e) {
            System.out.println("PaymentService: " + e.getMessage());
            return List.of("Оплата временно недоступна");
        }
    }
}
