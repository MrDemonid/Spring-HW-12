package mr.demonid.auth.server.configs;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

/**
 * Устанавливаем ключ для подписи Jwt-токенов.
 */

@Configuration
public class JwtConfig {

    /**
     * Создаем бин кодирования (подписания) Jwt-токена. Для чего используем
     * приватный ключ из файла.
     */
    @Bean
    public JwtEncoder jwtEncoder() throws Exception {
        // загрузка приватного ключа из PEM-файла
        RSAPrivateKey privateKey = loadPrivateKey("private_key.pem");
        RSAPublicKey publicKey = generatePublicKeyFromPrivate(privateKey);  // генерируем публичный ключ из приватного
        RSAKey rsaKey = new RSAKey.Builder(publicKey)                       // создаем RSAKey
                .privateKey(privateKey)
                .build();
        // оборачиваем в JWKSet
        ImmutableJWKSet<SecurityContext> jwkSet = new ImmutableJWKSet<>(new JWKSet(rsaKey));
        return new NimbusJwtEncoder(jwkSet);
    }

    /**
     * Загрузка приватного ключа из PEM-файла.
     * @param filename Имя файла, находящегося в папке ресурсов.
     */
    private RSAPrivateKey loadPrivateKey(String filename) throws Exception {
        ClassPathResource resource = new ClassPathResource(filename);
        String key = new String(resource.getInputStream().readAllBytes());
        String privateKeyPEM = key                                          // убираем все лишнее
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        // на оставшихся данных создаем объект RSAPrivateKey
        byte[] keyBytes = Base64.getDecoder().decode(privateKeyPEM);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) kf.generatePrivate(spec);
    }

    /**
     * Генерация публичного ключа из приватного.
     */
    public RSAPublicKey generatePublicKeyFromPrivate(RSAPrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        BigInteger modulus = privateKey.getModulus();
        BigInteger publicExponent = BigInteger.valueOf(65537);      // общий публичный экспонент для RSA
        // собственно создание публичного ключа
        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulus, publicExponent);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
    }

}
