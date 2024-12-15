package mr.demonid.web.client.configs;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class FeignClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            // Получаем текущую аутентификацию из SecurityContext
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication instanceof JwtAuthenticationToken jwtToken) {
                // Если пользователь авторизован через Jwt
                Jwt jwt = jwtToken.getToken();
                requestTemplate.header("Authorization", "Bearer " + jwt.getTokenValue());
            } else if (authentication.getPrincipal() instanceof DefaultOidcUser oidcUser) {
                // Если пользователь авторизован через OIDC
                String tokenValue = oidcUser.getIdToken().getTokenValue();
                requestTemplate.header("Authorization", "Bearer " + tokenValue);
            }
            // Если пользователь не авторизован, не добавляем Authorization
        };
    }
}
