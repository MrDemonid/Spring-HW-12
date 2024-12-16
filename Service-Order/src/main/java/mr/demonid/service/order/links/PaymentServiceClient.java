package mr.demonid.service.order.links;

import mr.demonid.service.order.config.FeignClientConfig;
import mr.demonid.service.order.dto.PaymentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "PAYMENT-SERVICE", configuration = FeignClientConfig.class)
public interface PaymentServiceClient {

    @PostMapping("api/payment/transfer")
    ResponseEntity<Void> transfer(@RequestBody PaymentRequest request);
}
