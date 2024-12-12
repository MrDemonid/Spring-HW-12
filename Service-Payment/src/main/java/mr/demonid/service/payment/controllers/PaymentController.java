package mr.demonid.service.payment.controllers;

import mr.demonid.service.payment.domain.strategy.PaymentStrategy;
import mr.demonid.service.payment.domain.strategy.PaymentStrategyRegistry;
import mr.demonid.service.payment.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentStrategyRegistry strategyRegistry;

    @Autowired
    public PaymentController(PaymentService paymentService, PaymentStrategyRegistry strategyRegistry) {
        this.paymentService = paymentService;
        this.strategyRegistry = strategyRegistry;
    }

    @PostMapping("/do-pay")
    public ResponseEntity<String> processPayment(
            @RequestParam String strategyName,
            @RequestParam String userId,
            @RequestParam BigDecimal amount) {
        PaymentStrategy strategy = strategyRegistry.getStrategy(strategyName);
        if (strategy == null) {
            return ResponseEntity.badRequest().body("Указан неверный способ оплаты!");
        }
        paymentService.processPayment(userId, amount, strategy);
        return ResponseEntity.ok("Payment processed");
    }

    @GetMapping("/strategies")
    public ResponseEntity<List<String>> getAvailableStrategies() {
        return ResponseEntity.ok(strategyRegistry.getAvailableStrategies());
    }
}
