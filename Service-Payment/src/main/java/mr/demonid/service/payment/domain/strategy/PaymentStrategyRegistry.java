package mr.demonid.service.payment.domain.strategy;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Хранит все найденные бины стратегий оплаты.
 * Стратегии добавляются автоматически, достаточно определить их через @Component (или аналогично),
 * т.е. зарегистрировать в IoC.
 */
@Component
public class PaymentStrategyRegistry {
    private final Map<String, PaymentStrategy> strategies;

    public PaymentStrategyRegistry(List<PaymentStrategy> strategyList) {
        this.strategies = strategyList.stream()
            .collect(Collectors.toMap(
                strategy -> strategy.getClass().getSimpleName(),
                strategy -> strategy
            ));
    }

    public PaymentStrategy getStrategy(String strategyName) {
        return strategies.get(strategyName);
    }

    public List<String> getAvailableStrategies() {
        return List.copyOf(strategies.keySet());
    }
}