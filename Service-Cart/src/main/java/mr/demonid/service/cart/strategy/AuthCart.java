package mr.demonid.service.cart.strategy;

import lombok.AllArgsConstructor;
import mr.demonid.service.cart.domain.CartItemEntity;
import mr.demonid.service.cart.dto.CartItem;
import mr.demonid.service.cart.repositories.CartRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Корзина авторизированного пользователя.
 */
@Service
@AllArgsConstructor
public class AuthCart implements Cart {

    private CartRepository cartRepository;


    @Override
    public CartItem addItem(String userId, String productId, int quantity) {
        CartItemEntity item = cartRepository.findByUserIdAndProductId(userId, productId);
        if (item == null) {
            item = new CartItemEntity(userId, productId, 0);
        }
        item.setQuantity(item.getQuantity() + quantity);
        item = cartRepository.save(item);
        return new CartItem(item.getUserId(), item.getProductId(), item.getQuantity());
    }


    @Override
    public void removeItem(String userId, String productId) {
        cartRepository.deleteByUserIdAndProductId(userId, productId);
    }


    @Override
    public List<CartItem> getItems(String userId) {
        List<CartItemEntity> items = cartRepository.findByUserId(userId);
        return items.stream().map(item -> new CartItem(item.getUserId(), item.getProductId(), item.getQuantity())).toList();
    }

}
