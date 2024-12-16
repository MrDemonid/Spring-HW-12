package mr.demonid.service.cart.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.authentication.AnonymousAuthenticationToken;

import java.util.Arrays;

public class UserContext {

    /**
     * Проверяет, является ли текущий пользователь анонимом.
     */
    public static boolean isAnonymous() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    /**
     * Возвращает идентификатор текущего пользователя.
     * Для анонимных пользователей возвращается anon_id, 
     * для авторизованных извлекается user_id из JWT токена.
     */
    public static String getCurrentUserId(HttpServletRequest request) {
        if (isAnonymous()) {
            String id = getAnonymousId(request);
            System.out.println("  -- UserContext: Anonymous user = " + id);
            return id;
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        System.out.println("  -- UserContext: User: " + jwt.getClaim("user_id"));
        return jwt.getClaim("user_id");
    }

    public static String getAnonymousId(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        System.out.println("  -- cookies: " + Arrays.toString(cookies));
        if (cookies == null)
            return null;

        return Arrays.stream(cookies)
                .filter(cookie -> "ANON_ID".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

}
