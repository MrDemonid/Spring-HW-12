package mr.demonid.service.cart.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Корзина не авторизированного пользователя.
 * Хранится в памяти и пропадает при уходе пользователя с сайта.
 */
public class AnonymousCartState implements CartState {

    private final List<CartItem> cartItems = new ArrayList<>();

    @Override
    public void addItem(Long productId, int quantity) {
        cartItems.add(new CartItem(productId, quantity));
    }

    @Override
    public void removeItem(Long productId) {
        cartItems.removeIf(item -> item.getProductId().equals(productId));
    }

    @Override
    public List<CartItem> viewCart() {
        return Collections.unmodifiableList(cartItems);
    }
}
