package mr.demonid.service.cart.controllers;

import lombok.AllArgsConstructor;
import mr.demonid.service.cart.domain.AuthorizedCartState;
import mr.demonid.service.cart.domain.CartItem;
import mr.demonid.service.cart.repositories.CartRepository;
import mr.demonid.service.cart.services.CartContext;
import mr.demonid.service.cart.services.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final CartContext cartContext;
    private final CartRepository cartRepository;


    @PostMapping("/add")
    public ResponseEntity<String> addItem(@RequestParam Long productId, @RequestParam int quantity) {

        Long userId = cartService.getCurrentUserId();
        if (userId != null) {
            cartContext.setCartState(new AuthorizedCartState(userId, cartRepository));
        }
        cartContext.addItem(productId, quantity);
        return ResponseEntity.ok("Товар добавлен в корзину");
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeItem(@RequestParam Long productId) {
        Long userId = cartService.getCurrentUserId();
        if (userId != null) {
            cartContext.setCartState(new AuthorizedCartState(userId, cartRepository));
        }
        cartContext.removeItem(productId);
        return ResponseEntity.ok("Товар удален из корзины");
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> viewCart() {
        Long userId = cartService.getCurrentUserId();
        if (userId != null) {
            cartContext.setCartState(new AuthorizedCartState(userId, cartRepository));
        }
        return ResponseEntity.ok(cartContext.viewCart());
    }

}
