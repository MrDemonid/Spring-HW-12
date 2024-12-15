package mr.demonid.web.client.links;

import mr.demonid.web.client.configs.FeignClientConfig;
import mr.demonid.web.client.dto.CartItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Обращение к микросервису Cart-service.
 */
@FeignClient(name = "CART-SERVICE", configuration = FeignClientConfig.class)      // имя сервиса, под которым он зарегистрирован в Eureka
public interface CartServiceClient {

    @GetMapping("/api/cart/count")
    ResponseEntity<Integer> getItemQuantity();

    @PostMapping("/api/cart/add")
    ResponseEntity<CartItem> addItem(@RequestParam String productId, @RequestParam Integer quantity);

    }
