package mr.demonid.service.cart.strategy;

import mr.demonid.service.cart.dto.CartItem;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Корзина для анонимных пользователей.
 * Располагается в памяти и теряется при уходе пользователя.
 */
@Service
public class AnonCart implements Cart {

    private final Map<String, List<CartItem>> cartItems = new ConcurrentHashMap<>();


    @Override
    public CartItem addItem(String userId, String productId, int quantity) {
        List<CartItem> cart = cartItems.computeIfAbsent(userId, k -> new ArrayList<>());

        CartItem item = cart.stream().filter(cartItem -> cartItem.getProductId().equals(productId)).findFirst().orElse(null);
        if (item != null) {
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            item = new CartItem(userId, productId, quantity);
            cart.add(item);
        }
        return item;
    }

    @Override
    public void removeItem(String userId, String productId) {
        List<CartItem> cart = cartItems.get(userId);
        if (cart != null) {
            cart.removeIf(cartItem -> cartItem.getProductId().equals(productId));
            if (cart.isEmpty()) {
                cartItems.remove(userId);       // корзина текущего пользователя пуста, можно удалить её
            }
        }
    }

    @Override
    public List<CartItem> getItems(String userId) {
        return cartItems.getOrDefault(userId, Collections.emptyList());
    }

}
