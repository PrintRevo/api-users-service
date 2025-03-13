package com.printrevo.tech.userservice.config.security;

import com.printrevo.tech.commonsecurity.security.resolvers.DelegatingAuthenticationManagerResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private static final String[] AUTH_WHITELIST =
            {
                    "/", "/v3/api-docs/**",
                    "/swagger-ui/**", "/swagger-ui.html",
                    "/health", "/actuator/**",
                    "/readiness", "/liveness"
            };

    private final DelegatingAuthenticationManagerResolver authenticationManagerResolver;

    public WebSecurityConfig(DelegatingAuthenticationManagerResolver authenticationManagerResolver) {
        this.authenticationManagerResolver = authenticationManagerResolver;
    }

    @Bean
    @Primary
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors().and()
                .csrf().disable()
                .headers().frameOptions().disable().and()
                .httpBasic().disable()
                .authorizeRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry
                                .antMatchers(AUTH_WHITELIST).permitAll()
                                .anyRequest().permitAll())
                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(httpSecurityOAuth2ResourceServerConfigurer ->
                        httpSecurityOAuth2ResourceServerConfigurer
                                .authenticationManagerResolver(authenticationManagerResolver))
                .build();
    }
}
