package mr.demonid.service.cart.domain;

import lombok.AllArgsConstructor;
import mr.demonid.service.cart.repositories.CartRepository;

import java.util.List;

/**
 * Корзина авторизированного пользователя.
 * Хранится в базе данных.
 */
@AllArgsConstructor
public class AuthorizedCartState implements CartState {

    private final Long userId;
    private final CartRepository cartRepository;


    @Override
    public void addItem(Long productId, int quantity) {
        cartRepository.addToCart(userId, productId, quantity);
    }

    @Override
    public void removeItem(Long productId) {
        cartRepository.removeFromCart(userId, productId);
    }

    @Override
    public List<CartItem> viewCart() {
        return cartRepository.getCartItems(userId);
    }

}
