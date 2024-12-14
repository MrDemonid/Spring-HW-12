package mr.demonid.service.cart.domain;

import java.util.List;

/**
 * Состояние корзины.
 */
public interface CartState {

    void addItem(Long productId, int quantity);
    void removeItem(Long productId);
    List<CartItem> viewCart();
}