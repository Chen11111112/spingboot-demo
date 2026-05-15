package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable()) // 關閉 CSRF
                // 設定 API 的存取權限 (API 白名單)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/activity/**", "/login", "/register").permitAll() // 這些不用登入
                        .anyRequest().authenticated() // 其他都要登入
                );

        return http.build();
    }

    // 設定前端跨域白名單
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000" // React 預設
        ));

        // 允許哪些 HTTP 方法？
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        // 允許前端傳送哪些 Header？
        configuration.setAllowedHeaders(List.of("*"));

        // 是否允許前端攜帶 Cookie 或 Authorization Token (JWT)？ (實務上通常設為 true)
        configuration.setAllowCredentials(true);

        // 將這套設定套用到所有的 API 路徑 ("/**")
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
