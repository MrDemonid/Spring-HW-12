package mr.demonid.service.cart.services;

import lombok.Getter;
import lombok.Setter;
import mr.demonid.service.cart.domain.AnonymousCartState;
import mr.demonid.service.cart.domain.CartItem;
import mr.demonid.service.cart.domain.CartState;
import org.springframework.stereotype.Component;

import java.util.List;

@Setter
@Getter
@Component
public class CartContext {

    private CartState cartState;

    public CartContext() {
        this.cartState = new AnonymousCartState();          // по дефолту анонимный
    }

    public void addItem(Long productId, int quantity) {
        cartState.addItem(productId, quantity);
    }

    public void removeItem(Long productId) {
        cartState.removeItem(productId);
    }

    public List<CartItem> viewCart() {
        return cartState.viewCart();
    }
}
