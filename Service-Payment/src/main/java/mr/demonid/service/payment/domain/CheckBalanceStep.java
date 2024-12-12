package mr.demonid.service.payment.domain;

import mr.demonid.service.payment.dto.PaymentData;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CheckBalanceStep implements PaymentStep {

//    private final UserRepository userRepository;
//
//    public CheckBalanceStep(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }

    @Override
    public void execute(PaymentData data) {
        // Получаем текущий баланс пользователя
//        BigDecimal userBalance = userRepository.findBalanceByUserId(data.getUserId())
//                .orElse(BigDecimal.ZERO);
        BigDecimal userBalance = BigDecimal.valueOf(100L);

        if (data.getAmount().compareTo(userBalance) > 0) {
            data.setSuccess(false);
            data.setMessage("Недостаточно средств");
            System.out.println("Недостаточно средств у пользователя: " + data.getUserId());
        } else {
            System.out.println("Баланс в порядке у пользователя: " + data.getUserId());
        }
    }

}
