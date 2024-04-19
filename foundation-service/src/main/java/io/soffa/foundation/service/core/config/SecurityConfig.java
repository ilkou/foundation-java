package io.soffa.foundation.service.core.config;

import io.soffa.foundation.core.security.PlatformAuthManager;
import io.soffa.foundation.service.core.RequestFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
//@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    private final PlatformAuthManager authManager;
    private final String openApiAccess;

    public SecurityConfig(
        @Value("${app.openapi.access:permitAll}") String openApiAccess, PlatformAuthManager authManager
    ) {
        super();
        this.authManager = authManager;
        this.openApiAccess = openApiAccess;
    }

    @Bean
    @ConditionalOnMissingBean(CorsFilter.class)
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }


    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        //TODO: use openApiAccess to give correct access
        http
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/v3/**", "/swagger/*").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/**").permitAll()
                .anyRequest()
                .authenticated())
            .addFilterBefore(new RequestFilter(authManager), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
