package mr.demonid.service.cart.strategy;

import mr.demonid.service.cart.dto.CartItem;

import java.util.List;

/**
 * Заглушка, на случай если пользователя нельзя опознать.
 */
public class EmptyCart implements Cart {
    @Override
    public CartItem addItem(String userId, String productId, int quantity) {
        return null;
    }

    @Override
    public void removeItem(String userId, String productId) {
    }

    @Override
    public List<CartItem> getItems(String userId) {
        return List.of();
    }
}
