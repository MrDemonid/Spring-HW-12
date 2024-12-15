package mr.demonid.web.client.configs;

import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@Configuration
@Slf4j
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
                log.info("RequestInterceptor(): JWT token: {}", jwt.getTokenValue());
            } else if (authentication.getPrincipal() instanceof DefaultOidcUser oidcUser) {
                // Если пользователь авторизован через OIDC
                String tokenValue = oidcUser.getIdToken().getTokenValue();
                requestTemplate.header("Authorization", "Bearer " + tokenValue);
                log.info("RequestInterceptor(): OidcUser: {}", oidcUser.getIdToken().getTokenValue());
            } else {
                // Если пользователь не авторизован, не добавляем Authorization
                log.info("RequestInterceptor(): Anonymous");
            }
        };
    }
}
