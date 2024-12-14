package mr.demonid.service.cart.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.authentication.AnonymousAuthenticationToken;

public class UserContext {

    /**
     * Проверяет, является ли текущий пользователь анонимом.
     */
    public static boolean isAnonymous() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication instanceof AnonymousAuthenticationToken;
    }

    /**
     * Возвращает идентификатор текущего пользователя.
     * Для анонимных пользователей возвращается anon_id, 
     * для авторизованных извлекается user_id из JWT токена.
     */
    public static String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (isAnonymous()) {
            return (String) authentication.getPrincipal();      // возвращаем anon_id
        }
        Jwt jwt = (Jwt) authentication.getPrincipal();
        return jwt.getClaim("user_id");
    }

}
