package mr.demonid.service.cart.services;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * Фабрика корзин.
 * Возвращает определенный тип корзины, в зависимости от
 * текущей стратегии (пользователя).
 */
@Service
@AllArgsConstructor
public class CartFactory {

    /**
     * Возвращает корзину, в зависимости от типа активного пользователя.
     */
    public Cart getCart(HttpServletRequest request) {
        if (isAnonymous()) {
            String id = getAnonymousId(request);
            if (id != null) {
                return new AnonCart(id);
            }
            return new EmptyCart();   // кто-то без идентификатора
        }
        String id = getUserId();
        if (id != null) {
            return new AuthCart(id);
        }
        return new EmptyCart();   // кто-то без идентификатора
    }

    /**
     * Проверяет, является ли текущий пользователь анонимом.
     */
    public static boolean isAnonymous() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    /**
     * Возвращает ID пользователя из поля "user_id" Jwt-Токена.
     */
    public static String getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        return jwt.getClaim("user_id");
    }

    /**
     * Возвращает ID пользователя из поля "anon_id" куков
     */
    public static String getAnonymousId(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            return Arrays.stream(cookies)
                    .filter(cookie -> "ANON_ID".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
}


//public Cart getCart() {
//    if (UserContext.isAnonymous()) {
//        return anonCart;
//    }
//    return authCart;
//}
