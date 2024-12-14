package mr.demonid.service.cart.controllers;

import lombok.extern.slf4j.Slf4j;
import mr.demonid.service.cart.dto.CartItem;
import mr.demonid.service.cart.services.CartService;
import mr.demonid.service.cart.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер корзины.
 * Данные по авторизированным пользователям извлекаются из токена Jwt.
 * Для анонимных пользователей используются куки. При первом обращении генерируется
 * уникальный UUID и сохраняется в cookie с именем anon_id, который используется при
 * каждом обращении к корзине, пока клиент не разорвет связь.
 */
@RestController
@Slf4j
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<CartItem> addItem(@RequestParam String productId, @RequestParam int quantity) {
        log.info("add: product id = {}, quantity = {}, user id: = {}", productId, quantity, UserContext.getCurrentUserId());
        return ResponseEntity.ok(cartService.addItemToCart(productId, quantity));
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getItems() {
        log.info("get cart from user: {}", UserContext.getCurrentUserId());
        return ResponseEntity.ok(cartService.getCartItems());
    }


//    // Метод для получения товаров в корзине
//    @GetMapping
//    public ResponseEntity<List<CartItemEntity>> getCartItems() {
//        String userId = getCurrentUserId();
//
//        List<CartItemEntity> cartItems = cartService.getCartItems(userId);
//
//        return ResponseEntity.ok(cartItems);
//    }
//
//
//    // Получаем идентификатор пользователя или анонимного пользователя
//    private String getCurrentUserId() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication instanceof AnonymousAuthenticationToken) {
//            // Если пользователь анонимный, возвращаем anon_id
//            return authentication.getPrincipal().toString(); // В this case, anon_id
//        }
//
//        // Если пользователь авторизован, возвращаем user_id из JWT
//        return ((Jwt) authentication.getPrincipal()).getClaim("user_id");
//    }
}
