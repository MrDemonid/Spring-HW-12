package mr.demonid.service.payment.controllers;

import lombok.AllArgsConstructor;
import mr.demonid.service.payment.domain.strategy.PaymentStrategy;
import mr.demonid.service.payment.domain.strategy.PaymentStrategyRegistry;
import mr.demonid.service.payment.dto.PaymentRequest;
import mr.demonid.service.payment.dto.StrategyInfo;
import mr.demonid.service.payment.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentStrategyRegistry strategyRegistry;


    /**
     * Резервирование средств и проверка доступности способа платежа.
     * @param request Данные о платеже.
     */
    @PostMapping("api/payment/reservation")
    public ResponseEntity<Void> reservation(@RequestBody PaymentRequest request) {

    }

    /**
     * Списание зарезервированных средств.
     * @param orderId Идентификатор заказа.
     */
    @PostMapping("api/payment/transfer")
    public ResponseEntity<Void> transfer(@RequestBody UUID orderId) {

    }

    /**
     * Отмена резервирования средств и их возврат на счет пользователя.
     * @param orderId  Идентификатор заказа.
     * @return
     */
    @PostMapping("api/payment/rollback")
    ResponseEntity<Void> rollback(@RequestBody UUID orderId) {

    }

//
//
//    @PostMapping("/do-pay")
//    public ResponseEntity<String> processPayment(
//            @RequestParam String strategyName,
//            @RequestParam String userId,
//            @RequestParam BigDecimal amount) {
//        PaymentStrategy strategy = strategyRegistry.getStrategy(strategyName);
//        if (strategy == null) {
//            return ResponseEntity.badRequest().body("Указан неверный способ оплаты!");
//        }
//        paymentService.processPayment(userId, amount, strategy);
//        return ResponseEntity.ok("Payment processed");
//    }

    @GetMapping("/strategies")
    public ResponseEntity<List<StrategyInfo>> getAvailableStrategies() {
        return ResponseEntity.ok(strategyRegistry.getAvailableStrategies());
    }
}
