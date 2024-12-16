package mr.demonid.service.cart.services;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
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

    private final ObjectProvider<AnonCart> anonCartProvider;
    private final ObjectProvider<AuthCart> authCartProvider;
    private final EmptyCart emptyCart;
    /**
     * Возвращает корзину, в зависимости от типа активного пользователя.
     */
    public Cart getCart(HttpServletRequest request) {
        if (isAnonymous()) {
            String id = getAnonymousId(request);
            if (id != null) {
                // создаем новый экземпляр корзины
                AnonCart anonCart = anonCartProvider.getObject();
                anonCart.setUserId(id);
                return anonCart;
            }
            return emptyCart;   // кто-то без идентификатора
        }
        String id = getUserId();
        if (id != null) {
            // создаем новый экземпляр корзины для аутентифицированного пользователя
            AuthCart authCart = authCartProvider.getObject();
            authCart.setUserId(id);
            return authCart;
        }
        return emptyCart;   // кто-то без идентификатора
    }

    /**
     * Проверяет, является ли текущий пользователь анонимом.
     */
    private static boolean isAnonymous() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    /**
     * Возвращает ID пользователя из поля "user_id" Jwt-Токена.
     */
    private static String getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        return jwt.getClaim("user_id");
    }

    /**
     * Возвращает ID пользователя из поля "anon_id" куков
     */
    private static String getAnonymousId(HttpServletRequest request) {
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
