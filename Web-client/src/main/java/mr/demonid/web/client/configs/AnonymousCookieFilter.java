package mr.demonid.web.client.configs;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class AnonymousCookieFilter extends OncePerRequestFilter {

    private static final String COOKIE_NAME = "ANON_ID";
    private static final int COOKIE_MAX_AGE = 7 * 24 * 60 * 60; // 7 дней

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("--> " + request.getRequestURI());
        // Проверяем наличие cookie
        boolean hasAnonymousCookie = false;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (COOKIE_NAME.equals(cookie.getName())) {
                    hasAnonymousCookie = true;
                    break;
                }
            }
        }

        // Если cookie нет, добавляем его
        if (!hasAnonymousCookie) {
            String anonId = UUID.randomUUID().toString();
            Cookie cookie = new Cookie(COOKIE_NAME, anonId);
            cookie.setPath("/");
            cookie.setHttpOnly(true); // Защита от XSS
            cookie.setMaxAge(COOKIE_MAX_AGE);
            response.addCookie(cookie);
        }

        filterChain.doFilter(request, response);
    }
}
