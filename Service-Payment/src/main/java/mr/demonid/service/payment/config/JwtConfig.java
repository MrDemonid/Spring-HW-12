package mr.demonid.service.payment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Настройка декодировщика Jwt-токенов.
 * Используется публичный ключ, из файла.
 */
@Configuration
public class JwtConfig {

    private static RSAPublicKey rsaPublicKey;

    @Bean
    public JwtDecoder jwtDecoder() throws Exception {
        if (rsaPublicKey == null) {
            rsaPublicKey = loadPublicKeyFromClassPath("public_key.pem");
        }
        return NimbusJwtDecoder.withPublicKey(rsaPublicKey).build();
    }

    /**
     * Загрузка публичного ключа из файла.
     * Файл должен быть в папке ресурсов.
     */
    private RSAPublicKey loadPublicKeyFromClassPath(String fileName) throws Exception {
        ClassPathResource resource = new ClassPathResource(fileName);
        byte[] keyBytes = Files.readAllBytes(resource.getFile().toPath());
        // обрезаем лишнее и декодируем из Base64
        byte[] decodedKey = Base64.getDecoder().decode(new String(keyBytes)
                .replaceAll("-----\\w+ PUBLIC KEY-----", "")
                .replaceAll("\\s+", ""));

        // собственно генерируем публичный ключ
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);

        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }
}