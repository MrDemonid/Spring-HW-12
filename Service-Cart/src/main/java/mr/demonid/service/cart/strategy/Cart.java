package mr.demonid.service.cart.strategy;

import mr.demonid.service.cart.dto.CartItem;

import java.util.List;

public interface Cart {

    CartItem addItem(String userId, String productId, int quantity);
    void removeItem(String userId, String productId);
    List<CartItem> getItems(String userId);
}
