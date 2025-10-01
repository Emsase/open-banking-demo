package dev.demo.openbank.apigateway.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${security.jwt.secret}")
    String secret;

    @Bean
    SecurityFilterChain filter(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(reg -> reg
                        .requestMatchers("/actuator/**", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth -> oauth.jwt(Customizer.withDefaults()));
        return http.build();
    }

    @Bean
    org.springframework.security.oauth2.jwt.JwtDecoder jwtDecoder() {
        SecretKeySpec key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(key).build();
    }
}
