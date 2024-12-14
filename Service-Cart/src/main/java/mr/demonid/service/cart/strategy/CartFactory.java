package mr.demonid.service.cart.strategy;

import lombok.AllArgsConstructor;
import mr.demonid.service.cart.utils.UserContext;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class CartFactory {

    private AuthCart authCart;
    private AnonCart anonCart;


    /**
     * Возвращает корзину, в зависимости от типа активного пользователя.
     */
    public Cart getCart() {
        if (UserContext.isAnonymous()) {
            return anonCart;
        }
        return authCart;
    }


}
