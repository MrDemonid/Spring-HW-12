package mr.demonid.service.cart.services;

import lombok.AllArgsConstructor;
import mr.demonid.service.cart.dto.CartItem;
import mr.demonid.service.cart.strategy.CartFactory;
import mr.demonid.service.cart.utils.UserContext;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class CartService {

    private CartFactory cartFactory;

    /**
     * Возвращает список товаров в корзине текущего пользователя.
     */
    public List<CartItem> getCartItems() {
        return cartFactory.getCart().getItems(UserContext.getCurrentUserId());
    }

    /**
     * Добавляет товар в корзину текущего пользователя.
     * @param productId Код товара
     * @param quantity  Количество.
     */
    public CartItem addItemToCart(String productId, int quantity) {
        return cartFactory.getCart().addItem(UserContext.getCurrentUserId(), productId, quantity);
    }

    /**
     * Возвращает кол-во товаров в корзине пользователя.
     */
    public Integer getCartItemQuantity() {
        List<CartItem> cartItems = getCartItems();
        return cartItems.size();
    }

}
