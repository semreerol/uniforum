package com.bunyaminkalkan.api.config;

import com.bunyaminkalkan.api.security.JwtAuthenticationEntryPoint;
import com.bunyaminkalkan.api.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    private JwtAuthenticationEntryPoint handler;

    public SecurityConfig(JwtAuthenticationEntryPoint handler) {
        this.handler = handler;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling((exceptionHandling) -> exceptionHandling.authenticationEntryPoint(handler))
                .sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests
                                .requestMatchers("/auth/**").permitAll()
                                .requestMatchers("/users/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/posts").permitAll()
                                .requestMatchers(HttpMethod.POST, "/posts").authenticated()
                                .requestMatchers(HttpMethod.GET, "/posts/{postId}").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/posts/{postId}").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/posts/{postId}").authenticated()
                                .requestMatchers(HttpMethod.GET, "/comments").permitAll()
                                .requestMatchers(HttpMethod.POST, "/comments").authenticated()
                                .requestMatchers(HttpMethod.GET, "/comments/{commentId}").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/comments/{commentId}").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/comments/{commentId}").authenticated()
                                .anyRequest().permitAll());

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
