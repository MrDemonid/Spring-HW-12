package mr.demonid.web.client.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Arrays;
import java.util.UUID;

public class IdnUtil {

    /**
     * Возвращает идентификатор авторизированного пользователя.
     */
    public static String getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtToken) {
            return jwtToken.getToken().getClaimAsString("user_id");
        } else if (authentication.getPrincipal() instanceof DefaultOidcUser oidcUser) {
            return oidcUser.getIdToken().getClaimAsString("user_id");
        }
        return null;
    }

    /**
     * Возвращает идентификатор анонимного пользователя, которого
     * ранее пометили в куки.
     */
    public String getAnonymousId(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> "ANON_ID".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    /**
     * Добавление куки "ANON_ID" для анонимного пользователя.
     * Но лучше это делать через фильтры (как и реализовано
     * в этом клиенте).
     */
    public void setAnonymousCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("ANON_ID", UUID.randomUUID().toString());
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7 дней
        response.addCookie(cookie);
    }


}
