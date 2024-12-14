package mr.demonid.service.cart.repositories;

import mr.demonid.service.cart.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByUserId(Long userId);

    void deleteByUserIdAndProductId(Long userId, Long productId);
}
//
///**
// * Пока заглушка.
// */
//@Repository
//public class CartRepository {
//
//
//    public void addToCart(Long userId, Long productId, int quantity) {
//    }
//
//    public void removeFromCart(Long userId, Long productId) {
//    }
//
//    public List<CartItem> getCartItems(Long userId) {
//        return Collections.emptyList();
//    }
//}
