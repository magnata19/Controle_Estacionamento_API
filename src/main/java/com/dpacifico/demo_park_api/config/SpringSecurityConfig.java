package com.dpacifico.demo_park_api.config;

import com.dpacifico.demo_park_api.jwt.JwtAuthenticationEntryPoint;
import com.dpacifico.demo_park_api.jwt.JwtAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableMethodSecurity
@EnableWebMvc //necessaria para trabalhar com sistema de segurança
@Configuration
public class SpringSecurityConfig {

    private static final String[] DOCUMENTATION_API = {
      "/docs/index.html","/docs-park.html","/docs-park/**","/v3/api-docs/**",
            "/swagger-ui-custom.html","/swagger-ui.html", "/swagger-ui/**",
            "/**.html","/webjars/**", "/configuration/**", "/swagger-resources/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) //desabilitar stateful
                .formLogin(form -> form.disable()) //desabilitar o form
                .httpBasic(basic -> basic.disable())
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers(HttpMethod.POST, "/api/v1/usuarios").permitAll() //definir permissoes de acesso
                                .requestMatchers(HttpMethod.POST, "/api/v1/auth").permitAll()
                                .requestMatchers(DOCUMENTATION_API).permitAll()
                                .anyRequest().authenticated() //pedir autenticacao pra qualquer request
                ).sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ).addFilterBefore(
                        jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class
                ).exceptionHandling(ex ->
                        ex.authenticationEntryPoint(new JwtAuthenticationEntryPoint()))
                .build();
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
