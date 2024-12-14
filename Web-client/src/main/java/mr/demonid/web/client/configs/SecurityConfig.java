package mr.demonid.web.client.configs;

import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)                      // Отключаем CSRF для запросов API
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/index/**", "/images/**").permitAll()  // Главная и публичные ресурсы
                        .anyRequest().authenticated()  // Остальные требуют аутентификации
                )
//                .oauth2Login(oauth2 -> oauth2
//                        .loginPage("/oauth2/authorization/apm-client")  // Путь для авторизации OAuth2
//                )
                .oauth2Login(Customizer.withDefaults()); // Настройка OAuth2 Login
        return http.build();
    }

}
