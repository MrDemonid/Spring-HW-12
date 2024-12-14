package mr.demonid.service.cart.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

/**
 * Добавляет в SecurityContext поле anon_id, для не авторизированных пользователей.
 */
@Component
@Slf4j
public class AnonymousUserFilter extends OncePerRequestFilter {

    private static final String COOKIE_NAME = "anon_id";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("AnonymousUserFilter: {}", request.getRequestURI());
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            // Получаем, или генерируем anon_id
            String anonId = Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                    .filter(cookie -> cookie.getName().equals(COOKIE_NAME))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElseGet(() -> {
                        // это первый вызов анона, создаем ему уникальный anon_id
                        String newId = UUID.randomUUID().toString();
                        Cookie cookie = new Cookie(COOKIE_NAME, newId);
                        cookie.setPath("/");
                        cookie.setHttpOnly(true);
                        response.addCookie(cookie);
                        return newId;
                    });

            // Создаём аутентификацию для анонима
            AnonymousAuthenticationToken authentication = new AnonymousAuthenticationToken(
                    "anonymousKey", anonId, AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);    // продолжаем цепочку фильтров
    }
}
